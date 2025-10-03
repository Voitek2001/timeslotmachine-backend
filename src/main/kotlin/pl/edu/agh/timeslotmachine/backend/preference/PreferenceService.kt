package pl.edu.agh.timeslotmachine.backend.preference

import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.util.algorithm.subtractFrom
import kotlin.jvm.optionals.getOrNull

@Service
class PreferenceService(
    private val prefRepository: PreferenceRepository
) {
    fun findById(id: Preference.Id) =
        prefRepository.findById(id)

    fun findById(userId: EID, concreteEventId: EID) =
        findById(Preference.Id(userId, concreteEventId)).getOrNull()

    fun findFetchedById(id: Preference.Id) =
        prefRepository.findAndFetchAllById(id)

    fun findFetchedById(userId: EID, concreteEventId: EID) =
        prefRepository.findAndFetchAllById(Preference.Id(userId, concreteEventId))

    fun findAll(spec: Specification<Preference>): List<Preference> =
        prefRepository.findAll(spec)

    fun findByUserId(id: EID) =
        prefRepository.findByUserId(id)

    fun findByGroupIdAndUserId(groupId: EID, userId: EID, isAssigned: Boolean? = null) =
        prefRepository.findByGroupIdAndUserId(groupId, userId, isAssigned)

    fun findByGroupId(id: EID) =
        prefRepository.findByGroupId(id)

    fun getAssigned(groupId: EID, userId: EID) =
        findByGroupIdAndUserId(groupId, userId, isAssigned = true)

    fun getFetchedOrCreate(userId: EID, concreteEventId: EID): Preference = findFetchedById(
        userId, concreteEventId
    ) ?: Preference(Preference.Id(userId, concreteEventId), 0, false).let {
        prefRepository.saveIncomplete(it)
        getFetchedOrCreate(userId, concreteEventId)
    }

    // TODO: check if the group has the corresponding status
    // TODO: create a separate EntityGraph: fetch up to group
    fun assignPreference(userId: EID, concreteEventId: EID) = getFetchedOrCreate(userId, concreteEventId).apply {
        if (isAssigned)
            throw PreferenceException.Kind.AlreadyAssigned()
        
        if (!isAssignable(this))
            throw PreferenceException.Kind.NotAssignable()
        
        isAssigned = true
        prefRepository.save(this)
    }

    // TODO: check if the group has the corresponding status
    fun withdrawPreference(userId: EID, concreteEventId: EID) = findById(userId, concreteEventId)?.apply {
        if (!isAssigned)
            throw PreferenceException.Kind.NotAssigned()
        isAssigned = false
        prefRepository.save(this)
    } ?: throw PreferenceException.Kind.NotFound()

    private fun isAssignable(pref: Preference) = getAssigned(
        pref.concreteEvent!!.event.group.id!!, pref.user!!.id!!
    ).any { it.concreteEvent!!.isConflict(pref.concreteEvent!!) }.not()

    // TODO: check if the group has the corresponding status
    @Transactional
    fun reassign(req: ReassignPreferenceRequest) = prefRepository.findByGroupIdAndUserId(
        req.groupId, req.userId
    ).mapTo(HashSet()) { pref ->
        if (pref.id.concreteEventId in req.assignedConcreteEventIds) {
            if (!pref.isAssigned) {
                pref.isAssigned = true
                prefRepository.save(pref)
            }
        } else if (pref.isAssigned) {
            pref.isAssigned = false
            prefRepository.save(pref)
        }

        pref.id.concreteEventId
    }.subtractFrom(req.assignedConcreteEventIds).forEach { newConcreteEventId ->
        assignPreference(req.userId, newConcreteEventId)
    }

    fun save(preference: Preference) =
        prefRepository.save(preference)

    fun saveAll(preferences: Iterable<Preference>): List<Preference> =
        prefRepository.saveAll(preferences)
}