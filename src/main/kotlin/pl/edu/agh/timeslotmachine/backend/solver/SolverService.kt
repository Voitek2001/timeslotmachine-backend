package pl.edu.agh.timeslotmachine.backend.solver

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.preference.Preference
import pl.edu.agh.timeslotmachine.backend.preference.PreferenceSpecification
import pl.edu.agh.timeslotmachine.backend.preference.PreferenceService
import pl.edu.agh.timeslotmachine.backend.solver.ilp.ILPSolver

@Service
class SolverService(
    private val preferenceService: PreferenceService
) {
    @Transactional
    fun solveFor(groupId: EID, postAction: () -> Unit) {
        val preferences = preferenceService.findAll(
            PreferenceSpecification().getPreferenceByGroupId(groupId)
        ).onEach { it.isAssigned = false }

        val allPreferences = preferences + createEmptyPreferences(preferences)

        ILPSolver().schedule(allPreferences).also {
            preferenceService.saveAll(it)
        }

        postAction()
    }


    fun createEmptyPreferences(preferencesFromDb: List<Preference>): List<Preference> {
        val existingPairs = mutableSetOf<Pair<Any, Any>>() // Assuming user and concreteEvent are of Any type, replace with actual types if known

        preferencesFromDb.forEach { preference ->
            val user = preference.user
            val conEvent = preference.concreteEvent
            if (user != null && conEvent != null) {
                existingPairs.add(user to conEvent)
            }
        }

        return preferencesFromDb.flatMap { preference ->
            val currUser = preference.user!!
            val currEvent = preference.concreteEvent?.event

            currEvent?.concreteEvents?.filter { conEvent ->
                val newPair = currUser to conEvent
                if (newPair !in existingPairs) {
                    existingPairs.add(newPair)
                    true
                } else {
                    false
                }
            }?.map { conEvent ->
                Preference(
                    user = currUser,
                    concreteEvent = conEvent,
                    points = 0,
                    isAssigned = false
                )
            } ?: emptyList()
        }
    }
}