package pl.edu.agh.timeslotmachine.backend.event

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.io.StringWriter

@Converter
class PointLimitsConverter(
    private val mapper: ObjectMapper
) : AttributeConverter<PointLimits, String> {
    override fun convertToDatabaseColumn(limits: PointLimits): String {
        val writer = StringWriter()
        mapper.writeValue(writer, limits)
        return writer.toString()
    }

    override fun convertToEntityAttribute(dbData: String): PointLimits = mapper.readValue(
        dbData,
        object : TypeReference<PointLimits>() {}
    )
}