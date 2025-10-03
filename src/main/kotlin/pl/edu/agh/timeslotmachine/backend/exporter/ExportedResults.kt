package pl.edu.agh.timeslotmachine.backend.exporter

import org.springframework.http.MediaType

data class ExportedResults<T>(
    val data: T,
    val type: MediaType,
    val name: String
)