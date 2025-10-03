package pl.edu.agh.timeslotmachine.backend.exchange.dto

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.group.overview.WeekType
import pl.edu.agh.timeslotmachine.backend.instructor.Instructor
import pl.edu.agh.timeslotmachine.backend.place.Place
import pl.edu.agh.timeslotmachine.backend.term.Term

data class ExchangeInfo(
    val conEventId: EID,
    val eventName: String,
    val firstTerm: Term,
    val instructor: Instructor,
    val place: Place,
    val weekDay: Int,
    val weekType: WeekType,
)
