package pl.edu.agh.timeslotmachine.backend.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.JwtException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

private val logger = KotlinLogging.logger {}

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(value = [ResponseStatusException::class])
    fun responseStatusExceptionHandler(req: HttpServletRequest, e: ResponseStatusException) =
        ResponseEntity(ExceptionInfo(e), e.statusCode).also { logException(e) }

    @ExceptionHandler(value = [JwtException::class])
    fun authorizationExceptionHandler(req: HttpServletRequest, e: JwtException) = ResponseEntity(
        ExceptionInfo(ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message, e)),
        HttpStatus.UNAUTHORIZED
    )

    @ExceptionHandler(value = [Exception::class])
    fun defaultExceptionHandler(req: HttpServletRequest, e: Exception) = ResponseEntity(
        ExceptionInfo(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message, e)),
        HttpStatus.INTERNAL_SERVER_ERROR
    ).also { logException(e) }

    private fun logException(e: Exception) =
        logger.error { "${e}\n${e.stackTraceToString()}" }
}