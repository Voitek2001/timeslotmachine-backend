package pl.edu.agh.timeslotmachine.backend.group.webEdit

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.event.PointLimits

data class EventEditInfo(
    val id: EID?,
    val name: String?,
    val shortName: String?,
    val description: String?,
    val color: String?,
    val pointLimits: PointLimits?,
    val concreteEvents: List<ConcreteEventEditInfo>?,
    val users: List<EID>? // TODO: just EID?
)