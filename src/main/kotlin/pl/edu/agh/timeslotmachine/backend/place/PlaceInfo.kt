package pl.edu.agh.timeslotmachine.backend.place

import org.springframework.data.geo.Point
import pl.edu.agh.timeslotmachine.backend.core.EID

data class PlaceInfo(
    val id: EID? = null,
    val name: String? = null,
    val description: String? = null,
    val localization: Point? = null,
    val room: String? = null
)