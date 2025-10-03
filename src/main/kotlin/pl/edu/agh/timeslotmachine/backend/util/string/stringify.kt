package pl.edu.agh.timeslotmachine.backend.util.string

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.javaMethod

// TODO: collections, superclasses
fun stringify(o: Any?): String {
    if (o == null)
        return "null"

    val clazz = o::class

    return clazz.simpleName + clazz.declaredMemberProperties.map {
        val obj = it.getter.call(o)

        // if toString is declared in Object - that means that it
        // hasn't been overridden => the object is not printable
        fun isPrintable() = obj?.run {
            this::class.memberFunctions.single { function ->
                function.name == "toString"
            }.javaMethod!!.declaringClass != Any::class.java
        } ?: true

        fun isCollection() = obj?.run {
            obj is Collection<*>
        } ?: false

        "${it.name}=${
            if (isCollection())
                "<collection:${obj!!::class.simpleName}>"
            else if (isPrintable())
                obj
            else stringify(obj)
        }"
    }.toString()
}