package pl.edu.agh.timeslotmachine.backend.annotation

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import pl.edu.agh.timeslotmachine.backend.auth.SwaggerAuthConfig

@SecurityRequirement(name = SwaggerAuthConfig.AUTHORIZATION_HEADER)
annotation class SwaggerSessionSecurity