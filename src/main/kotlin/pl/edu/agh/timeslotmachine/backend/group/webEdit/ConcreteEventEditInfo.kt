package pl.edu.agh.timeslotmachine.backend.group.webEdit

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import org.springframework.http.HttpStatus
import pl.edu.agh.timeslotmachine.backend.event.ActivityForm
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.exception.BackendException
import java.time.LocalDateTime

data class ConcreteEventEditInfo(
    val id: EID?,
    val placeId: EID?,
    val userLimit: Int?,
    val activityForm: ActivityForm?,

    // TODO: simplification: start/end/periodicity/instructor instead of a list
    //  of terms most likely will (or even should) be changed in the future, when
    //  the frontend will be ready
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val termCount: Int?,
    val periodicity: Periodicity?,
    val instructorId: EID?
) {
    enum class Periodicity {
        Weekly,
        EverySecondWeek;

        @JsonValue
        fun toJsonValue() = when (this) {
            Weekly -> "weekly"
            EverySecondWeek -> "every_second_week"
        }

        companion object {
            @JsonCreator
            fun fromJsonValue(jsonVal: String) = when (jsonVal) {
                Weekly.toJsonValue() -> Weekly
                EverySecondWeek.toJsonValue() -> EverySecondWeek
                else -> throw InvalidPeriodicityValueException(jsonVal)
            }
        }

        class InvalidPeriodicityValueException(
            value: String
        ) : BackendException(
            HttpStatus.BAD_REQUEST,
            "Unknown periodicity value '$value'"
        )
    }
}