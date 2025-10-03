package pl.edu.agh.timeslotmachine.backend.user

import org.springframework.http.HttpStatus
import pl.edu.agh.timeslotmachine.backend.exception.BackendException

class UserAlreadyExistException : BackendException(
    HttpStatus.BAD_REQUEST,
    "User with the same username or email already exist"
)