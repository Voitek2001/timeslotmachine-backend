package pl.edu.agh.timeslotmachine.backend.auth

import jakarta.persistence.EntityManager
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseCookie
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.timeslotmachine.backend.security.jwt.JwtService
import pl.edu.agh.timeslotmachine.backend.user.UserService
import java.time.LocalDateTime
import java.util.UUID

@Service
class AuthService(
    private val userService: UserService,
    private val tokenRepository: AuthTokenRepository,
    private val entityManager: EntityManager,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
) {


    fun getToken(sessionId: SessionId) =
        tokenRepository.findByToken(UUID.fromString(sessionId))?.apply {
            if (isExpired()) throw UnauthorizedException.Kind.TokenExpired()
        } ?: throw UnauthorizedException.Kind.TokenNotFound()

    fun getUser(sessionId: SessionId) =
        getToken(sessionId).user

    @Transactional
    fun auth(credentials: Credentials, response: HttpServletResponse) = userService.findByCredentials(credentials).let { user ->
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(credentials.username, credentials.password))
        val userDetails = userService.loadUserByUsername(credentials.username)
        val jwt = jwtService.generateToken(userDetails)
        setRefreshTokenCookie(response, jwtService.generateRefreshToken(userDetails))
        AuthToken(user, LocalDateTime.now().plusDays(TOKEN_LIFESPAN_DAYS)).let {
            tokenRepository.save(it)
            tokenRepository.flush()
            entityManager.refresh(it)
            AuthController.TokenResponse(
                sessionId = it.token,
                token = jwt
            )
        }
    }

    fun createUser(newUser: NewUserInfo) =
        userService.createWithDefaultRole(newUser)

    @Transactional
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse): AuthController.TokenResponse {
        val refreshToken = request.cookies?.find { it.name == REFRESH_TOKEN_COOKIE_NAME }?.value ?: throw UnauthorizedException.Kind.TokenNotFound()
        jwtService.validateToken(refreshToken)
        val username = jwtService.extractUsername(refreshToken)
        val userDetails = userService.loadUserByUsername(username)
        val newAccessToken = jwtService.generateToken(userDetails)
        val newRefreshToken = jwtService.generateRefreshToken(userDetails)
        setRefreshTokenCookie(response, newRefreshToken)
        val user = userService.findByUsername(username)
        AuthToken(user, LocalDateTime.now().plusDays(TOKEN_LIFESPAN_DAYS)).let {
            tokenRepository.save(it)
            tokenRepository.flush()
            entityManager.refresh(it)
            return AuthController.TokenResponse(
                sessionId = it.token,
                token = newAccessToken
            )
        }

    }

    fun logout(response: HttpServletResponse) {
        disableRefreshCookie(response)
    }

    private fun disableRefreshCookie(response: HttpServletResponse) {
        val cookie = ResponseCookie.from("refreshToken", "")
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .path("/")
            .maxAge(0)
            .build()

        response.setHeader("Set-Cookie", cookie.toString())
    }


    private fun setRefreshTokenCookie(response: HttpServletResponse, refreshToken: String) {
        val cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .path("/")
            .maxAge(jwtService.REFRESH_EXPIRATION_TIME.toLong())
            .build()

        response.addHeader("Set-Cookie", cookie.toString())
    }

    companion object {
        const val TOKEN_LIFESPAN_DAYS = 7L
        const val REFRESH_TOKEN_COOKIE_NAME = "refreshToken"

    }
}