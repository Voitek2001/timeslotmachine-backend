package pl.edu.agh.timeslotmachine.backend.auth

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerAuthConfig {
    
    @Bean
    fun openApiConfig(): OpenAPI = OpenAPI()
        .addSecurityItem(SecurityRequirement())
        .components(Components()
            .addSecuritySchemes(AUTHORIZATION_HEADER, securityScheme(AUTHORIZATION_HEADER)))

    private fun securityScheme(name: String) = SecurityScheme()
        .type(SecurityScheme.Type.APIKEY)
        .`in`(SecurityScheme.In.HEADER)
        .name(name)

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
    }
}