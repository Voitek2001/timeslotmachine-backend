package pl.edu.agh.timeslotmachine.backend.group.overview

import pl.edu.agh.timeslotmachine.backend.core.EID

data class PreferenceSummary(
    val concreteEventId: EID,
    val points: Int,
    val isImpossible: Boolean,
    val impossibilityJustification: String?
)
