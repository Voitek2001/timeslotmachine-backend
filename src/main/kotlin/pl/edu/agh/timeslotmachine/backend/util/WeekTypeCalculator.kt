package pl.edu.agh.timeslotmachine.backend.util

import pl.edu.agh.timeslotmachine.backend.group.overview.WeekType
import pl.edu.agh.timeslotmachine.backend.term.Term
import java.time.LocalDateTime
import java.time.temporal.WeekFields
import java.util.*


val POLAND_LOCALE = Locale("pl", "PL")

// assume that odd week of current year is A and even is B

fun getWeekType(dateTime: LocalDateTime) =
    if (dateTime.get(WeekFields.of(POLAND_LOCALE).weekOfWeekBasedYear()) % 2 == 0)
        WeekType.B
    else WeekType.A

fun getWeekType(terms: Iterable<Term>): WeekType {
    if (terms.all { getWeekType(it.start) == WeekType.A })
        return WeekType.A
    if (terms.all { getWeekType(it.start) == WeekType.B })
        return WeekType.B
    return WeekType.AB
}
