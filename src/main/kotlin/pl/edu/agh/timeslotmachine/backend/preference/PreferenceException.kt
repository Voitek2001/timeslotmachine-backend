package pl.edu.agh.timeslotmachine.backend.preference

import org.springframework.http.HttpStatus
import pl.edu.agh.timeslotmachine.backend.exception.BackendException

class PreferenceException private constructor(
    kind: Kind,
    status: HttpStatus
) : BackendException(
    status,
    kind.name
) {
    enum class Kind {
        AlreadyAssigned,
        NotAssignable,
        NotAssigned,
        NotFound;
        
        operator fun invoke() = PreferenceException(this, when (this) {
            NotFound -> HttpStatus.NOT_FOUND
            else -> HttpStatus.BAD_REQUEST
        })
    }
}