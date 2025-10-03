package pl.edu.agh.timeslotmachine.backend.event

import org.springframework.stereotype.Service
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.diffIdsEntities
import pl.edu.agh.timeslotmachine.backend.term.TimeSlot
import pl.edu.agh.timeslotmachine.backend.group.overview.ConcreteEventSummary
import pl.edu.agh.timeslotmachine.backend.group.overview.DaySlot
import pl.edu.agh.timeslotmachine.backend.group.overview.WeekDay
import pl.edu.agh.timeslotmachine.backend.term.toHourMinutePair
import pl.edu.agh.timeslotmachine.backend.util.getWeekType
import pl.edu.agh.timeslotmachine.backend.util.algorithm.multipleGroupBy
import kotlin.jvm.optionals.getOrNull

@Service
class ConcreteEventService(
    private val repository: ConcreteEventRepository
) {
    fun getById(id: EID) =
        repository.findById(id).getOrNull() ?: throw ConcreteEventNotFoundException(id)

    fun getByIds(ids: Collection<EID>): List<ConcreteEvent> = repository.findAllById(ids).also { concretes ->
        if (concretes.size != ids.size)
            throw ConcreteEventNotFoundException(diffIdsEntities(ids, concretes))
    }

    fun findByGroupId(id: EID) =
        repository.findByGroupId(id)

    fun saveAll(concretes: Iterable<ConcreteEvent>) =
        repository.saveAll(concretes)

    fun getWeeklyOverview(concretes: List<ConcreteEvent>) = run {
        // group by week day
        concretes.multipleGroupBy { concreteEvent ->
            concreteEvent.terms.mapTo(HashSet()) { it.start.dayOfWeek }
        }.map { (day, concretes) ->
            // group by time range
            day to concretes.multipleGroupBy { concreteEvent ->
                // calculate time slots based on terms
                concreteEvent.terms.mapTo(HashSet()) {
                    TimeSlot(it.start.toHourMinutePair(), it.end.toHourMinutePair())
                }
            }
        }.map { (day, timeSlots) ->
            // transform
            WeekDay(day.value, timeSlots.map { (time, concretes) ->
                DaySlot(time, concretes.map { concrete ->
                    ConcreteEventSummary(
                        concreteEvent = concrete,
                        event = concrete.event,
                        weekType = getWeekType(concrete.terms),
                        instructor = concrete.terms.first().instructor // TODO: likely will be changed in the future
                    )
                })
            }.sortedBy { it.time.start })
        }
    }

    fun getByEventIdAndActivityForm(eventId: EID, activityForm: ActivityForm) =
        repository.findByEventIdAndActivityForm(eventId, activityForm)

}