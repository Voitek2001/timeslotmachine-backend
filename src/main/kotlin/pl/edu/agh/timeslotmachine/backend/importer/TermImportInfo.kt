package pl.edu.agh.timeslotmachine.backend.importer

import java.time.LocalDateTime

data class TermImportInfo(
    val start: LocalDateTime,
    val end: LocalDateTime,
    val instructor: InstructorImportInfo
)
