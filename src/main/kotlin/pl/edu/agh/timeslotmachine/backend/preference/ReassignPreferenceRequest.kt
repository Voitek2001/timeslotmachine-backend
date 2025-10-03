package pl.edu.agh.timeslotmachine.backend.preference

import pl.edu.agh.timeslotmachine.backend.core.EID

data class ReassignPreferenceRequest(
    val userId: EID,
    val groupId: EID,
    val assignedConcreteEventIds: Set<EID>
)