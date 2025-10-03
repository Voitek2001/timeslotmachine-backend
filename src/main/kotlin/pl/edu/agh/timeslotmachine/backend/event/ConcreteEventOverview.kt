package pl.edu.agh.timeslotmachine.backend.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonUnwrapped
import pl.edu.agh.timeslotmachine.backend.instructor.Instructor
import pl.edu.agh.timeslotmachine.backend.term.TimeSlot

data class ConcreteEventOverview(
    @JsonUnwrapped
    @JsonIgnoreProperties("userLimit")
    val concreteEvent: ConcreteEvent,

    @JsonIgnoreProperties("pointLimits")
    val event: Event,

    val uniqueTerms: Set<TimeSlot>,

    val uniqueInstructors: Set<Instructor>
)