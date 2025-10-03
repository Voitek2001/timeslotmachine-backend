package pl.edu.agh.timeslotmachine.backend.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/login/")
    fun login(
        @RequestBody root: CredentialsRoot,
        response: HttpServletResponse,
    ) = authService.auth(root.credentials, response)

    @PostMapping("/register/")
    fun register(
        @RequestBody newUserInfoRoot: NewUserInfoRoot
    ) = authService.createUser(newUserInfoRoot.newUser)

    @PostMapping("/logout/")
    fun logout(
        response: HttpServletResponse
    ) = authService.logout(response)

    @PostMapping("/refresh/")
    fun refresh(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) = authService.refreshToken(request, response)

    data class NewUserInfoRoot(
        val newUser: NewUserInfo
    )

    data class CredentialsRoot(
        val credentials: Credentials
    )

    data class TokenResponse(
        val sessionId: UUID,
        val success: Boolean = true,
        val token: String? = null,
    )
}