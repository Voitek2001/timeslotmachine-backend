package pl.edu.agh.timeslotmachine.backend.event

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

// Roni, 13:21 15.06.24:
// nie bardzo lubię obsługiwać zmienne klucze
class PointLimitsSerializer : JsonSerializer<PointLimits>() {
    override fun serialize(value: PointLimits, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeObject(value.map { FlatPointLimits(it.key, it.value.minPointsPerActivity) })
    }

    private data class FlatPointLimits(
        val activityForm: ActivityForm,
        val minPointsPerActivity: Int
    )
}