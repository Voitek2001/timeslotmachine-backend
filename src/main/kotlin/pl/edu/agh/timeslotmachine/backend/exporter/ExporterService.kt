package pl.edu.agh.timeslotmachine.backend.exporter

import org.springframework.stereotype.Service
import pl.edu.agh.timeslotmachine.backend.core.EID

@Service
class ExporterService(
    private val iCalendarExporterService: ICalendarExporterService
) {
    fun export(userId: EID, groupId: EID, type: ExporterType): ExportedResults<*> = when (type) {
        ExporterType.ICalendar -> iCalendarExporterService.export(userId, groupId)
    }
}