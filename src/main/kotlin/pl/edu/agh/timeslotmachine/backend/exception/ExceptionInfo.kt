package pl.edu.agh.timeslotmachine.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.timeslotmachine.backend.response.ResponseStatusInfo
import java.time.LocalDateTime

class ExceptionInfo(
    val errorName: String?,
    message: String?,
    status: Int,
    statusDescription: String,
    timestamp: LocalDateTime
) : ResponseStatusInfo(
    status = status,
    statusDescription = statusDescription,
    timestamp = timestamp,
    message = message
) {
    constructor(ex: ResponseStatusException) : this(
        errorName = ex::class.simpleName,
        message = ex.reason,
        status = ex.statusCode.value(),
        statusDescription = HttpStatus.valueOf(ex.statusCode.value()).reasonPhrase,
        timestamp = LocalDateTime.now()
    )
}