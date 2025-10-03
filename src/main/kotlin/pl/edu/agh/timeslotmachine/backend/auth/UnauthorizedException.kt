package pl.edu.agh.timeslotmachine.backend.auth

import org.springframework.http.HttpStatus
import pl.edu.agh.timeslotmachine.backend.exception.BackendException

class UnauthorizedException(
    kind: Kind
) : BackendException(
    HttpStatus.UNAUTHORIZED,
    kind.name
) {
    enum class Kind {
        MissingHeader,
        TokenExpired,
        TokenNotFound;

        operator fun invoke() =
            UnauthorizedException(this)
    }
}