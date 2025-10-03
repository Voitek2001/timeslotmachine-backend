package pl.edu.agh.timeslotmachine.backend.exchange

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.timeslotmachine.backend.checker.*
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEventOverview
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEventService
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.exchange.dto.*
import pl.edu.agh.timeslotmachine.backend.group.GroupStatus
import pl.edu.agh.timeslotmachine.backend.preference.PreferenceService
import pl.edu.agh.timeslotmachine.backend.term.TimeSlot
import pl.edu.agh.timeslotmachine.backend.term.toHourMinutePair
import pl.edu.agh.timeslotmachine.backend.user.UserService
import pl.edu.agh.timeslotmachine.backend.user.User
import pl.edu.agh.timeslotmachine.backend.util.getWeekType
import java.util.*
import kotlin.collections.HashSet
import kotlin.jvm.optionals.getOrNull


@Service
class ExchangeService(
    private val prefService: PreferenceService,
    private val preferenceService: PreferenceService,
    private val userService: UserService,
    private val conEventService: ConcreteEventService,
    private val exchangeRepository: ExchangeRepository
) : Checker() {

    fun getExchangeData(conEventId: EID, user: User) = require(
        ConEventInGroupWithStatus(conEventId, GroupStatus.Exchange),
        UserHasPreferenceAssigned(conEventId, user.id!!)
    ).then { getExistingExchanges(conEventId).let { existingExchanges ->
        ExchangeData(
            exchangesInfo = getExchangeInfoByConEventId(conEventId, user)
                .filterExistingExchanges(existingExchanges)
                .filterExchangeUniqueForUser(conEventId, user)
                .filterUserSpecificExchanges(conEventId),
            existingExchangesInfo = existingExchanges,
            currEventExchangeInfo = getConEventExchangeInfo(conEventId)
        )
    }}

    fun getOverview(user: User, groupId: EID) = require(
        GroupHasStatus(groupId, GroupStatus.Exchange),
        UserInGroup(user.id!!, groupId)
    ).then {
        exchangeRepository.getByInitiatorOrAcceptorAndGroupId(user.id!!, groupId)
            .map {
                ExchangeOverviewInfo(
                    it.id!!,
                    ExchangeInfo(
                        it.conEventOffered.id!!,
                        it.conEventOffered.event.name,
                        it.conEventOffered.terms.first(),
                        it.conEventOffered.terms.first().instructor,
                        it.conEventOffered.place,
                        it.conEventOffered.terms.first().start.dayOfWeek.value,
                        getWeekType(it.conEventOffered.terms)
                    ),
                    ExchangeInfo(
                        it.conEventRequested.id!!,
                        it.conEventRequested.event.name,
                        it.conEventRequested.terms.first(),
                        it.conEventRequested.terms.first().instructor,
                        it.conEventRequested.place,
                        it.conEventRequested.terms.first().start.dayOfWeek.value,
                        getWeekType(it.conEventRequested.terms)
                    ),
                    it.status,
                    it.exchangeIdentifier
                )
            }
    }

    fun createExchange(createExchangeInfo: CreateExchangeInfo, user: User) = require(
        ConEventInGroupWithStatus(createExchangeInfo.conEventOfferedId!!, GroupStatus.Exchange),
        ConEventInGroupWithStatus(createExchangeInfo.conEventRequestedIds, GroupStatus.Exchange),
        ConEventsInSameGroup(createExchangeInfo.conEventOfferedId, createExchangeInfo.conEventRequestedIds),
        UserHasPreferenceAssigned(createExchangeInfo.conEventOfferedId, user.id!!)
    ).then {
        if (createExchangeInfo.isPrivate && createExchangeInfo.conEventRequestedIds.size > 1) {
            throw ExchangeException.Kind.CannotCreate()
        }
        createExchangeInfo.conEventRequestedIds.forEach {
            if (exchangeRepository.existsByConEventOfferedId(it)) {
                throw ExchangeException.Kind.CannotCreate()
            }

        }
        conEventService.getByIds(createExchangeInfo.conEventRequestedIds).forEach {
            exchangeRepository.save(
                Exchange(
                    exchangeInitiator = user,
                    conEventOffered = conEventService.getById(createExchangeInfo.conEventOfferedId),
                    conEventRequested = it,
                    status = ExchangeStatus.Pending,
                    isPrivate = createExchangeInfo.isPrivate,
                    exchangeIdentifier = UUID.randomUUID()
                )
            )
        }
    }

    @Transactional
    fun makeExchange(exchangeId: EID, user: User) = require(
        ExchangeInGroupWithStatus(exchangeId, GroupStatus.Exchange),
        ExchangeHasStatus(exchangeId, ExchangeStatus.Pending)
    ).then {
        exchangeRepository.run {
            stored<Exchange>(exchangeId).let {
                validateMakeExchange(user, it)

                findAllByConEventOfferedIdAndExchangeInitiatorId(
                    it.conEventOffered.id!!,
                    user.id!!
                ).forEach { offered ->
                    setExchangeStatus(offered.id!!, ExchangeStatus.Cancelled)
                }

                setExchangeAcceptor(it.id!!, user)
                setExchangeStatus(it.id!!, ExchangeStatus.Done)

                reassignPreferences(
                    it.exchangeInitiator.id!!, user.id!!,
                    it.conEventOffered.id!!, it.conEventRequested.id!!
                )
            }
        }
    }

    @Transactional
    fun cancelExchange(exchangeId: EID) = require(
        ExchangeInGroupWithStatus(exchangeId, GroupStatus.Exchange),
        ExchangeHasStatus(exchangeId, ExchangeStatus.Pending)
    ).then {
        stored<Exchange>(exchangeId).let {
            exchangeRepository.setExchangeStatus(exchangeId, ExchangeStatus.Cancelled)
        }
    }

    @Transactional
    fun makePrivateExchange(privateExchangeInfo: PrivateExchangeInfo, user: User) =
        makeExchange(getByExchangeIdentifier(privateExchangeInfo.exchangeToken).id!!, user) // TODO: getExchangeIdByToken?

    @Transactional
    fun exchange(req: ExchangeRequest) = req.swaps.forEach {
        reassignPreferences(req.userId1, req.userId2, it.concreteEventId1, it.concreteEventId2)
    }

    fun getEventsForReassign(groupId: EID, userId: EID): ReassignInfo {
        val userConEventsOverview = userService.getConcreteEventsOverview(groupId, userId)

        val userPreferences = preferenceService.findByUserId(userId).filter { it.isAssigned }
        val userEvents = userPreferences.map { it.concreteEvent!! }

        val allGroupEvents = conEventService.findByGroupId(groupId).filter { !userEvents.contains(it) }
        val allGroupConEventsOverview = allGroupEvents.map { conEvent ->
            ConcreteEventOverview(
                concreteEvent = conEvent,
                event = conEvent.event,
                uniqueTerms = conEvent.terms.mapTo(HashSet()) {
                    TimeSlot(it.start.toHourMinutePair(), it.end.toHourMinutePair())
                },
                uniqueInstructors = conEvent.terms.mapTo(HashSet()) {
                    it.instructor
                }
            )
        }

        return ReassignInfo(
            userConEvents = userConEventsOverview,
            allGroupConEvents = allGroupConEventsOverview
        )
    }

    fun getById(exchangeId: EID) = exchangeRepository.findById(exchangeId).getOrNull() ?: throw ExchangeException.Kind.Invalid()

    private fun reassignPreferences(
        initiatorId: EID,
        acceptorId: EID,
        conEventOfferedId: EID,
        conEventRequestedId: EID
    ) = prefService.run {
        withdrawPreference(initiatorId, conEventOfferedId)
        withdrawPreference(acceptorId, conEventRequestedId)
        assignPreference(initiatorId, conEventRequestedId)
        assignPreference(acceptorId, conEventOfferedId)
    }

    private fun List<ConcreteEvent>.filterConflicts(user: User, conEvent: ConcreteEvent) = filter {
        !areConflicts(user, it, conEvent)
    }

    private fun getExchangeInfoByConEventId(conEventId: EID, user: User) = conEventService.getById(conEventId).let { conEvent ->
        conEventService.getByEventIdAndActivityForm(conEvent.event.id!!, conEvent.activityForm)
            .filterConflicts(user, conEvent)
            .map {
                ExchangeInfo(
                    it.id!!,
                    it.event.name,
                    it.terms.first(),
                    it.terms.first().instructor,
                    it.place,
                    it.terms.first().start.dayOfWeek.value,
                    getWeekType(it.terms)
                )
            }
        }

    private fun getExistingExchanges(conEventId: EID) = exchangeRepository.findNotAcceptedAndPendingAndNotPrivate(conEventId).map {
        ExistingExchangeInfo(
            it.id!!,
            it.status,
            ExchangeInfo(
                it.conEventOffered.id!!,
                it.conEventOffered.event.name,
                it.conEventOffered.terms.first(),
                it.conEventOffered.terms.first().instructor,
                it.conEventOffered.place,
                it.conEventOffered.terms.first().start.dayOfWeek.value,
                getWeekType(it.conEventOffered.terms)
            ),
            it.exchangeIdentifier
        )
    }

    private fun getByExchangeIdentifier(exchangeIdentifier: UUID) =
        exchangeRepository.getByExchangeIdentifier(exchangeIdentifier) ?: throw ExchangeException.Kind.TokenNotFound()

    private fun getConEventExchangeInfo(conEventId: EID) = conEventService.getById(conEventId).let { conEvent ->
        ExchangeInfo(
            conEvent.id!!,
            conEvent.event.name,
            conEvent.terms.first(),
            conEvent.terms.first().instructor,
            conEvent.place,
            conEvent.terms.first().start.dayOfWeek.value,
            getWeekType(conEvent.terms)
        )
    }

    private fun List<ExchangeInfo>.filterExchangeUniqueForUser(conEventId: EID, user: User) = filter {
        !exchangeRepository.existsByOfferedAndRequestedAndInitiator(conEventId, it.conEventId, user)
    }

    private fun List<ExchangeInfo>.filterUserSpecificExchanges(conEventId: EID) = filter {
        it.conEventId != conEventId
    }

    private fun List<ExchangeInfo>.filterExistingExchanges(existingExchanges: List<ExistingExchangeInfo>) = filter {
        !existingExchanges.map { existingExchange ->
            existingExchange.offered.conEventId }.contains(it.conEventId)
    }

    private fun validateMakeExchange(user: User, exchange: Exchange) {
        if (areConflicts(user, exchange.conEventOffered, exchange.conEventRequested) ||
            areConflicts(exchange.exchangeInitiator, exchange.conEventRequested, exchange.conEventOffered)) {
            throw ExchangeException.Kind.Invalid()
        }
    }

    private fun areConflicts(user: User, newConcreteEvent: ConcreteEvent, oldConcreteEvent: ConcreteEvent): Boolean {
        val groupId = newConcreteEvent.event.group.id!!
        return prefService.getAssigned(groupId, user.id!!)
            .filter { it.concreteEvent!!.id != oldConcreteEvent.id }
            .any { newConcreteEvent.isConflict(it.concreteEvent!!)
        }
    }
}