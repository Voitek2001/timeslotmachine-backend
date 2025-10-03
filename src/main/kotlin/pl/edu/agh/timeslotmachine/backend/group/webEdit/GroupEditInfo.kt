package pl.edu.agh.timeslotmachine.backend.group.webEdit

import pl.edu.agh.timeslotmachine.backend.core.EID

// TODO: I don't like the name... Maybe GroupMutateInfo?
data class GroupEditInfo(
    val id: EID?,
    val name: String?,
    val description: String?,
    val maxPointsPerConcrete: Int?,
    val events: List<EventEditInfo>?
)