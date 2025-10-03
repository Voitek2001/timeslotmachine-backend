package pl.edu.agh.timeslotmachine.backend.exporter

import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.TimeZoneRegistryFactory
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.parameter.Cn
import net.fortuna.ical4j.model.property.Attendee
import net.fortuna.ical4j.model.property.ProdId
import net.fortuna.ical4j.model.property.immutable.ImmutableCalScale
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.preference.PreferenceService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ICalendarExporterService(
    private val preferenceService: PreferenceService
) {
    // TODO: check group status
    fun export(userId: EID, groupId: EID) = preferenceService.findByGroupIdAndUserId(
        groupId = groupId,
        userId = userId,
        isAssigned = true
    ).flatMap {
        makeVEvents(it.concreteEvent!!)
    }.let {
        ExportedResults(
            data = toCalendar(it),
            type = MediaType("text", "calendar"),
            name = "calendar_${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)}.ical"
        )
    }

    private fun makeVEvents(conEvent: ConcreteEvent) = conEvent.terms.map { term ->
        VEvent(term.start, term.end, conEvent.event.name).apply {
            Attendee().apply {
                add<Attendee>(Cn(term.instructor.fullName))
            }.also {
                add<VEvent>(it)
            }
        }
    }

    private fun toCalendar(events: List<VEvent>) = Calendar().apply {
        add<Calendar>(ProdId("-//Events Calendar//iCal4j 1.0//EN"))
        add<Calendar>(ImmutableCalScale.GREGORIAN)

        getVTimeZone().also { tz ->
            events.forEach {
                it.add<VEvent>(tz)
                add<Calendar>(it)
            }
        }
    }

    private fun getVTimeZone() =
        TimeZoneRegistryFactory.getInstance().createRegistry().getTimeZone(TIMEZONE).vTimeZone

    companion object {
        // TODO: move it to some config file
        const val TIMEZONE = "Europe/Warsaw"
    }
}