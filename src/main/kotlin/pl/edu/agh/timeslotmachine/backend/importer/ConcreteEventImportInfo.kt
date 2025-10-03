package pl.edu.agh.timeslotmachine.backend.importer

import pl.edu.agh.timeslotmachine.backend.event.ActivityForm

data class ConcreteEventImportInfo(
    val limit: Int,
    val activityForm: ActivityForm,
    val place: PlaceImportInfo,
    val terms: Set<TermImportInfo>
)
