package pl.edu.agh.timeslotmachine.backend.group.webEdit

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.timeslotmachine.backend.checker.Checker
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.event.Event
import pl.edu.agh.timeslotmachine.backend.group.Group
import pl.edu.agh.timeslotmachine.backend.group.GroupCRUDService
import pl.edu.agh.timeslotmachine.backend.group.overview.GroupOverviewService
import pl.edu.agh.timeslotmachine.backend.group.overview.ReassignSummary
import pl.edu.agh.timeslotmachine.backend.instructor.InstructorService
import pl.edu.agh.timeslotmachine.backend.place.PlaceService
import pl.edu.agh.timeslotmachine.backend.term.Term
import pl.edu.agh.timeslotmachine.backend.user.IndexNo
import pl.edu.agh.timeslotmachine.backend.user.UserService
import pl.edu.agh.timeslotmachine.backend.util.algorithm.validateNotNull

@Service
class GroupWebEditService(
    private val groupService: GroupCRUDService,
    private val placeService: PlaceService,
    private val instructorService: InstructorService,
    private val groupCRUDService: GroupCRUDService,
    private val userService: UserService,
    private val groupOverviewService: GroupOverviewService
): Checker() {
    @Transactional
    fun createGroup(groupInfo: GroupEditInfo) {
        // 1st step: validate data
        createGroupValidateInput(groupInfo)

        // 2nd step: create entities
        Group(
            name = groupInfo.name!!,
            description = groupInfo.description!!,
            maxPointsPerConcrete = groupInfo.maxPointsPerConcrete!!,
        ).apply {
            events += groupInfo.events!!.map { eventInfo ->
                Event(
                    name = eventInfo.name!!,
                    shortName = eventInfo.shortName!!,
                    description = eventInfo.description!!,
                    color = eventInfo.color!!,
                    pointLimits = eventInfo.pointLimits!!,
                    group = this
                ).apply {
                    concreteEvents += eventInfo.concreteEvents!!.map { concreteInfo ->
                        ConcreteEvent(
                            place = placeService.getById(concreteInfo.placeId!!),
                            userLimit = concreteInfo.userLimit!!,
                            activityForm = concreteInfo.activityForm!!,
                            event = this
                        ).apply {
                            terms = genTerms(concreteInfo, this)
                        }
                    }
                }
            }
        }.also {
            groupService.save(it)
        }
    }

    private fun genTerms(
        concreteInfo: ConcreteEventEditInfo,
        concreteEvent: ConcreteEvent
    ): MutableSet<Term> = (0..<concreteInfo.termCount!!).mapTo(HashSet()) {
        val daysToAdd = it * when (concreteInfo.periodicity!!) {
            ConcreteEventEditInfo.Periodicity.Weekly -> 7
            ConcreteEventEditInfo.Periodicity.EverySecondWeek -> 14
        }.toLong()

        Term(
            start = concreteInfo.startDate!!.plusDays(daysToAdd),
            end = concreteInfo.endDate!!.plusDays(daysToAdd),
            concreteEvent = concreteEvent,
            instructor = instructorService.getById(concreteInfo.instructorId!!)
        )
    }

    private fun createGroupValidateInput(group: GroupEditInfo) {
        group.apply {
            validateNotNull(
                ::name,
                ::description,
                ::maxPointsPerConcrete,
                ::events
            )
        }

        group.events!!.forEach { event ->
            event.apply {
                validateNotNull(
                    ::name,
                    ::shortName,
                    ::description,
                    ::color,
                    ::pointLimits,
                    ::concreteEvents
                )
            }

            event.concreteEvents!!.forEach { concrete ->
                concrete.apply {
                    validateNotNull(
                        ::placeId,
                        ::userLimit,
                        ::activityForm,
                        ::startDate,
                        ::endDate,
                        ::termCount,
                        ::periodicity,
                        ::instructorId
                    )
                }
            }
        }
    }

    @Transactional
    fun assignUserForGroup(reassignUser: ReassignUserForGroupInfo) {
        if (groupCRUDService.containsUserByIndex(reassignUser.groupId, reassignUser.userIndexNo)) {
            throw ReassignUserException.Kind.UserAlreadyAssigned()
        }
        groupCRUDService.addUserByIndexNo(reassignUser.groupId, reassignUser.userIndexNo)
    }

    @Transactional
    fun unassignUserForGroup(reassignUser: ReassignUserForGroupInfo) {
        groupCRUDService.unassignUserByIndexNo(reassignUser.groupId, reassignUser.userIndexNo)
    }

    fun getReassignUserOverview(groupId: EID, indexNo: IndexNo) =
        ReassignSummary(
            groupService.getById(groupId),
            userService.getByIndexNo(indexNo)
        )

}
