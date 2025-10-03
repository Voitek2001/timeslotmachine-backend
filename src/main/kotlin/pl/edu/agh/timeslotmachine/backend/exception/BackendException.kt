package pl.edu.agh.timeslotmachine.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class BackendException(
    status: HttpStatus,
    message: String
) : ResponseStatusException(status, message)