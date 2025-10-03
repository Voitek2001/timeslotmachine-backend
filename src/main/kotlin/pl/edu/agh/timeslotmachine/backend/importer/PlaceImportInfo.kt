package pl.edu.agh.timeslotmachine.backend.importer

import org.springframework.data.geo.Point

data class PlaceImportInfo(
    val name: String,
    val description: String,
    val room: String,
    val localization: Point?
)
