package pl.edu.agh.timeslotmachine.backend.group.overview

import pl.edu.agh.timeslotmachine.backend.term.TimeSlot

data class DaySlot(
    val time: TimeSlot,
    val concreteEvents: List<ConcreteEventSummary>
)