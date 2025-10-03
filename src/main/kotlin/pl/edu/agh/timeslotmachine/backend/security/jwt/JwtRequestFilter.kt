package pl.edu.agh.timeslotmachine.backend.security.jwt

import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import pl.edu.agh.timeslotmachine.backend.auth.AUTH_USER_ATTRIB
import pl.edu.agh.timeslotmachine.backend.security.AUTHORIZATION_HEADER
import pl.edu.agh.timeslotmachine.backend.security.AUTHORIZATION_HEADER_PREFIX
import pl.edu.agh.timeslotmachine.backend.user.UserService

@Component
class JwtRequestFilter(
    private val jwtService: JwtService,
    private val userService: UserService,
    @Qualifier("handlerExceptionResolver") private val resolver: HandlerExceptionResolver
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        try {
            if (userAlreadyAuthenticated()) {
                filterChain.doFilter(request, response)
                return
            }

            val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER) ?: throw JwtHeaderException.Kind.MissingTokenHeader()
            if (!authorizationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) throw JwtHeaderException.Kind.InvalidAuthorizationHeaderPrefix()

            val jwt = authorizationHeader.substring(AUTHORIZATION_HEADER_PREFIX.length)

            jwtService.validateToken(jwt) // throw if not valid

            val username = jwtService.extractUsername(jwt)
            val userDetails: UserDetails = userService.loadUserByUsername(username)
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.authorities
            ).apply {
                details = WebAuthenticationDetailsSource().buildDetails(request)
            }
            request.setAttribute(AUTH_USER_ATTRIB, userService.findByUsername(username))
            filterChain.doFilter(request, response)

        } catch (e: JwtHeaderException) {
            logger.error(e.message)
            resolver.resolveException(request, response, null, e)
        } catch (e: JwtException) {
            logger.error(e.message)
            resolver.resolveException(request, response, null, e)
        } catch (e: Exception) {
            logger.error(e.message)
        }

    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath == "/auth/login/" ||
                request.servletPath == "/auth/refresh/" ||
                request.servletPath == "/auth/register/" ||
                request.servletPath == "/auth/logout/"
    }

    private fun userAlreadyAuthenticated(): Boolean = SecurityContextHolder.getContext().authentication != null

}