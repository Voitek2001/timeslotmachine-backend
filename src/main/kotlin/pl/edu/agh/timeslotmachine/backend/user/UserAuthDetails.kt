package pl.edu.agh.timeslotmachine.backend.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserAuthDetails(
    private val authorities: MutableCollection<out GrantedAuthority>,
    private val password: String,
    private val username: String,
    private val isAccountNonExpired: Boolean,
    private val isAccountNonLocked: Boolean,
    private val isCredentialsNonExpired: Boolean,
    private val isEnabled: Boolean
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername() = username

    override fun isAccountNonExpired() = isAccountNonExpired

    override fun isAccountNonLocked() = isAccountNonLocked

    override fun isCredentialsNonExpired() = isCredentialsNonExpired

    override fun isEnabled() = isEnabled

}