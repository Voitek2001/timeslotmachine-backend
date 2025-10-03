package pl.edu.agh.timeslotmachine.backend.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsernamePasswordAuthenticationManager(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder
) : AuthenticationManager {

    override fun authenticate(authentication: Authentication?): Authentication {
        val auth = authentication ?: throw BadCredentialsException("Invalid username or password")

        val user = userDetailsService.loadUserByUsername(auth.name)
        val rawPassword = auth.credentials as? String ?: throw BadCredentialsException("Invalid credentials")

        if (!passwordEncoder.matches(rawPassword, user.password)) {
            throw BadCredentialsException("Invalid username or password")
        }

        return UsernamePasswordAuthenticationToken(auth.name, rawPassword, auth.authorities)
    }
}
