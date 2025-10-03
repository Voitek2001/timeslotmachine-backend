package pl.edu.agh.timeslotmachine.backend.checker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import pl.edu.agh.timeslotmachine.backend.checker.Checker.ParametrizedKey
import kotlin.reflect.KClass

@Component
class Checker {
    @Autowired
    private lateinit var appContext: ApplicationContext

    fun require(
        vararg checks: Check,
        throwOnFailure: Boolean = true,
        storage: CheckerStorage = CheckerStorage()
    ) = when (ENABLE_CHECKS) {
        true -> evaluateChecks(
            checks = checks,
            throwOnFailure = throwOnFailure,
            storage = storage
        )
        false -> Result(
            satisfied = true,
            storage = storage
        )
    }

    private fun evaluateChecks(
        vararg checks: Check,
        throwOnFailure: Boolean = true,
        storage: CheckerStorage = CheckerStorage()
    ) = CheckerContext(
        appContext = appContext,
        storage = storage
    ).let { context ->
        checks.all { check ->
            check(context).also {
                if (!it && throwOnFailure) throw RequirementNotSatisfiedException(check)
            }
        }.let { status ->
            Result(
                satisfied = status,
                storage = context.storage
            )
        }
    }

    data class Result(
        val satisfied: Boolean,
        val storage: CheckerStorage
    )

    data class ParametrizedKey<T : Any>(
        val klass: KClass<T>,
        val params: List<Any>
    ) {
        companion object {
            inline operator fun <reified T : Any> invoke(vararg params: Any) = ParametrizedKey(
                klass = T::class,
                params = params.toList()
            )
        }
    }

    companion object {
        // TODO: should be moved to config
        const val ENABLE_CHECKS: Boolean = true
    }
}

data class CheckerContext(
    val appContext: ApplicationContext,
    val storage: CheckerStorage
) {
    inline fun <reified T> bean() =
        appContext.getBean(T::class.java)
}

@Suppress("UNCHECKED_CAST")
class CheckerStorage {
    private val objects = HashMap<ParametrizedKey<*>, Any>()

    fun <T : Any> storedOrCreate(key: ParametrizedKey<T>, creator: (CheckerStorage) -> T) =
        storedOrNull(key) ?: store(key, creator(this))

    inline fun <reified T : Any> storedOrCreate(
        vararg params: Any,
        noinline creator: (CheckerStorage) -> T
    ) = storedOrCreate(ParametrizedKey<T>(*params), creator)

    fun <T : Any> storedOrNull(key: ParametrizedKey<T>) =
        objects[key] as T?

    fun <T : Any> stored(key: ParametrizedKey<T>) =
        objects[key] as T

    inline fun <reified T : Any> stored(vararg params: Any) =
        stored(ParametrizedKey<T>(*params))

    fun <T : Any> store(key: ParametrizedKey<T>, obj: T) = run {
        objects[key] = obj
        obj
    }

    inline fun <reified T : Any> store(obj: T, vararg params: Any) =
        store(ParametrizedKey<T>(*params), obj)
}

fun <R> Checker.Result.then(action: CheckerStorage.() -> R?) = when (satisfied) {
    true -> action(storage)
    false -> null
}