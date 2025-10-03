package pl.edu.agh.timeslotmachine.backend.util.algorithm

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.reflect.KProperty

fun <T : Any> T.validateNotNull(
    vararg properties: KProperty<*>
) = properties.forEach { prop ->
    prop.getter.call() ?: throw ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Property ${prop.name} of class ${this::class.simpleName} cannot be null"
    )
}.let { this }