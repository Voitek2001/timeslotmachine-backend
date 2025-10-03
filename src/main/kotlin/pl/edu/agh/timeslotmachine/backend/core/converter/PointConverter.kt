package pl.edu.agh.timeslotmachine.backend.core.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.data.geo.Point

@Converter
class PointConverter : AttributeConverter<Point?, String?> {
    override fun convertToDatabaseColumn(point: Point?): String? =
        point?.run { "($x,$y)" }

    override fun convertToEntityAttribute(str: String?): Point? = str?.run {
        substring(1, length - 1).split(',').let { coords ->
            Point(coords[0].toDouble(), coords[1].toDouble())
        }
    }
}