package pl.edu.agh.timeslotmachine.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import pl.edu.agh.timeslotmachine.backend.annotation.DebugOnly

@Configuration
class CorsConfig {
    @Bean
    @DebugOnly
    fun corsDebugFilter() = CorsFilter(UrlBasedCorsConfigurationSource().apply {
        registerCorsConfiguration("/**", CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedHeaders = listOf("*")
            allowedMethods = listOf("*")
        })
    })
}