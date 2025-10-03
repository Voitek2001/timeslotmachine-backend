package pl.edu.agh.timeslotmachine.backend.importer

import pl.edu.agh.timeslotmachine.backend.event.PointLimits

data class EventImportInfo(
    val name: String,
    val shortName: String,
    val description: String,
    val color: String,
    val pointLimits: PointLimits,
    val concreteEvents: Set<ConcreteEventImportInfo>
)
