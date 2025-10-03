package pl.edu.agh.timeslotmachine.backend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authorization.AuthorityAuthorizationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pl.edu.agh.timeslotmachine.backend.security.jwt.JwtRequestFilter
import pl.edu.agh.timeslotmachine.backend.user.Role


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val jwtRequestFilter: JwtRequestFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http {
            headers {
                contentTypeOptions { }
                xssProtection { }
                cacheControl { }
                httpStrictTransportSecurity { }
                frameOptions { }
            }
            cors { }
//            csrf {
//                csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse()
//                csrfTokenRequestHandler = CsrfTokenRequestAttributeHandler()
//                ignoringRequestMatchers(AntPathRequestMatcher("/csrf"))
//            }
//            logout {
//                logoutSuccessUrl = "/auth/logout/"
//                permitAll
//            }
            csrf { disable() }
            authorizeHttpRequests {
                authorize("/auth/**", permitAll)
                authorize("/groups/**", userAuth())
                authorize("/exchanges/**", userAuth())
                authorize("/users/**", userAuth()) // hasRole("USER")
                authorize("/admin/**", adminAuth())
                authorize(anyRequest, authenticated)
            }


            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtRequestFilter)
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
        }
        return http.build()
    }

//    @Bean
//    fun jwtRequestFilter(@Qualifier("handlerExceptionResolver") resolver: HandlerExceptionResolver): JwtRequestFilter = JwtRequestFilter(jwtService, userService, resolver)


    @Bean
    fun roleHierarchy(): RoleHierarchy = RoleHierarchyImpl().apply { setHierarchy("ROLE_ADMIN > ROLE_USER") } // TODO refactor: create role builder


    @Bean
    fun expressionHandler(): DefaultMethodSecurityExpressionHandler = DefaultMethodSecurityExpressionHandler().apply {
        setRoleHierarchy(roleHierarchy())
    }

    @Bean
    fun grantedAuthorityDefaults() : GrantedAuthorityDefaults = GrantedAuthorityDefaults(Role.DEFAULT_ROLE_PREFIX)



    private fun userAuth() = AuthorityAuthorizationManager.hasRole<RequestAuthorizationContext>(Role.User.name.uppercase()).apply {
        setRoleHierarchy(roleHierarchy())
    }

    private fun adminAuth() = AuthorityAuthorizationManager.hasRole<RequestAuthorizationContext>(Role.Admin.name.uppercase()).apply {
        setRoleHierarchy(roleHierarchy())
    }

}
