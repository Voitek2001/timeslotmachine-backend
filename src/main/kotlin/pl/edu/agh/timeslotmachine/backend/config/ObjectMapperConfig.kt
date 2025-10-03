package pl.edu.agh.timeslotmachine.backend.config

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfig {
    @Bean
    fun jsonCustomizer() = Jackson2ObjectMapperBuilderCustomizer { builder ->
        // By default, the Spring Boot configuration will disable
        // MapperFeature.DEFAULT_VIEW_INCLUSION: Feature that determines whether properties
        // that have no view annotations are included in JSON serialization views.
        // If enabled, non-annotated properties will be included.
        // See: https://www.javadoc.io/doc/com.fasterxml.jackson.core/jackson-databind/2.17.1/index.html
        builder.defaultViewInclusion(true)
    }
}