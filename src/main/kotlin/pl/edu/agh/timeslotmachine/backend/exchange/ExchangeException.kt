package pl.edu.agh.timeslotmachine.backend.exchange

import org.springframework.http.HttpStatus
import pl.edu.agh.timeslotmachine.backend.exception.BackendException

class ExchangeException(
    kind: Kind
) : BackendException(
    HttpStatus.BAD_REQUEST,
    kind.name
) {
    enum class Kind {
        CannotCancel,
        CannotCreate,
        TokenNotFound,
        Invalid;

        operator fun invoke() =
            ExchangeException(this)
    }
}