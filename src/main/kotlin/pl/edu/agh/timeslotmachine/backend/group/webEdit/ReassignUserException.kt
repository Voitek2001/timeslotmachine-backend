package pl.edu.agh.timeslotmachine.backend.group.webEdit

import org.springframework.http.HttpStatus
import pl.edu.agh.timeslotmachine.backend.exception.BackendException

class ReassignUserException(
    kind: Kind
) : BackendException(
    HttpStatus.BAD_REQUEST,
    kind.name
) {
    enum class Kind {
        UserAlreadyAssigned,
        UserNotAssigned;
        operator fun invoke() =
            ReassignUserException(this)
    }
}

