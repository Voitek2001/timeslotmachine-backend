package pl.edu.agh.timeslotmachine.backend.response

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

open class ResponseStatusInfo(
    val status: Int,
    val statusDescription: String,
    val timestamp: LocalDateTime,
    val message: String? = null
) {
    constructor(
        status: HttpStatus,
        message: String? = null
    ) : this(
        status = status.value(),
        statusDescription = status.reasonPhrase,
        timestamp = LocalDateTime.now(),
        message = message
    )
}
