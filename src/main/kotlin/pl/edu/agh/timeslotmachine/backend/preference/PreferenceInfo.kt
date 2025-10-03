package pl.edu.agh.timeslotmachine.backend.preference

import pl.edu.agh.timeslotmachine.backend.core.EID

data class PreferenceInfo(
    val concreteEventId: EID,
    val points: Int,
    val isImpossible: Boolean?,
    val impossibilityJustification: String?
)
