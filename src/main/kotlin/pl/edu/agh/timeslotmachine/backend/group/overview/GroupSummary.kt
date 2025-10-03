package pl.edu.agh.timeslotmachine.backend.group.overview

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.event.Event
import pl.edu.agh.timeslotmachine.backend.group.GroupStatus

data class GroupSummary(
    val id: EID,
    val name: String,
    val description: String,
    val maxPointsPerConcrete: Int,
    val events: List<Event>,
    val status: GroupStatus
)
