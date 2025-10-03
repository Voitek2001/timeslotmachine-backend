package pl.edu.agh.timeslotmachine.backend.security.jwt

import io.jsonwebtoken.JwtException

class JwtHeaderException(
    kind: Kind
) : JwtException(kind.name) {
    enum class Kind {
        MissingTokenHeader,
        InvalidAuthorizationHeaderPrefix;

        operator fun invoke() =
            JwtHeaderException(this)
    }
}