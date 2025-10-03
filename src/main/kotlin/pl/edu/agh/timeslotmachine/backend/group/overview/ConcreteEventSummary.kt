package pl.edu.agh.timeslotmachine.backend.group.overview

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonUnwrapped
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.event.Event
import pl.edu.agh.timeslotmachine.backend.instructor.Instructor

data class ConcreteEventSummary(
    @JsonUnwrapped
    val concreteEvent: ConcreteEvent,

    @JsonIgnoreProperties("pointLimits")
    val event: Event,

    val weekType: WeekType,

    // TODO: most likely a temporary solution, will be
    //  changed to list of instructors when the frontend
    //  will be ready
    val instructor: Instructor
)