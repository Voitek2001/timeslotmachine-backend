package pl.edu.agh.timeslotmachine.backend.user

import org.springframework.http.HttpStatus
import pl.edu.agh.timeslotmachine.backend.exception.BackendException

class InvalidCredentialsException : BackendException(
    HttpStatus.BAD_REQUEST,
    "Invalid credentials"
)