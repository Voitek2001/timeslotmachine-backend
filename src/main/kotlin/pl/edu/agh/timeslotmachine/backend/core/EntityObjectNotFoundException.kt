package pl.edu.agh.timeslotmachine.backend.core

import org.springframework.http.HttpStatus
import pl.edu.agh.timeslotmachine.backend.exception.BackendException
import kotlin.reflect.KClass

private fun getMessage(klass: KClass<*>, ids: List<EID>) =
    if (ids.size == 1) "${klass.simpleName} with id '${ids.first()}' cannot be found"
    else "${klass.simpleName}(s) with ids $ids cannot be found"

open class EntityObjectNotFoundException(
    message: String
) : BackendException(
    HttpStatus.NOT_FOUND,
    message
) {
    constructor(
        klass: KClass<*>,
        ids: List<EID>
    ) : this(getMessage(klass, ids))
}

fun diffIds(p1: Collection<EID>, p2: Collection<EID>) =
    p1 - p2.toSet()

fun <T : EntityObject> diffIdsEntities(p1: Collection<EID>, p2: Collection<T>) =
    diffIds(p1, p2.map { it.id!! })