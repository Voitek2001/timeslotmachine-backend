package pl.edu.agh.timeslotmachine.backend.term

import com.fasterxml.jackson.annotation.JsonValue
import java.time.LocalDateTime

data class HourMinutePair(
    val hour: Int,
    val minute: Int
) : Comparable<HourMinutePair> {
    override fun compareTo(other: HourMinutePair) =
        compareValuesBy(this, other, { it.hour }, { it.minute })
}

fun LocalDateTime.toHourMinutePair() =
    HourMinutePair(hour, minute)

data class TimeSlot(
    val start: HourMinutePair,
    val end: HourMinutePair
) {
    @JsonValue
    fun asJsonValue() = run {
        fun stringifyDate(p: HourMinutePair) =
            "${"%02d".format(p.hour)}:${"%02d".format(p.minute)}"
        "${stringifyDate(start)}-${stringifyDate(end)}"
    }
}
