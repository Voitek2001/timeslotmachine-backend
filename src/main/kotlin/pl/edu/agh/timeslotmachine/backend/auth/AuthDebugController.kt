package pl.edu.agh.timeslotmachine.backend.auth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import pl.edu.agh.timeslotmachine.backend.annotation.DebugRestController

@DebugRestController
class AuthDebugController(
    private val authService: AuthService
) {
    @GetMapping("/token")
    fun getToken(
        @RequestParam sessionId: SessionId
    ) = authService.getToken(sessionId)
}