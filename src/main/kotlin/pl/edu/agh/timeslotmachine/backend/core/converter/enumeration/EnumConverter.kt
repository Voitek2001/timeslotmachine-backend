package pl.edu.agh.timeslotmachine.backend.core.converter.enumeration

import jakarta.persistence.AttributeConverter
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance

abstract class EnumConverter<L>(
    private val klass: KClass<L>
) : AttributeConverter<L, RemoteEnum>
        where L : Enum<L>,
              L : RemoteConvertable<L>
{
    override fun convertToDatabaseColumn(attribute: L) =
        attribute.toRemote()

    @Suppress("UNCHECKED_CAST")
    override fun convertToEntityAttribute(dbData: RemoteEnum) =
        (klass.companionObjectInstance as LocalConvertable<L>?)?.run { toLocal(dbData) } ?: run {
            throw RuntimeException("Companion object is not defined or doesn't inherit from LocalConvertable")
        }
}