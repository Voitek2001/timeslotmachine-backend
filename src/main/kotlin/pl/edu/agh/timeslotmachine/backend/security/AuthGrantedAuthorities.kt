package pl.edu.agh.timeslotmachine.backend.security

import org.springframework.security.core.GrantedAuthority
import pl.edu.agh.timeslotmachine.backend.user.Role


class AuthGrantedAuthorities(
    private val role: Role,
) : GrantedAuthority {
    override fun getAuthority() = role.toAuthorityString()

    override fun toString() = role.name

}