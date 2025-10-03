package pl.edu.agh.timeslotmachine.backend.event

import org.springframework.stereotype.Service
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.diffIdsEntities
import kotlin.jvm.optionals.getOrNull

@Service
class EventService(
    private val eventRepository: EventRepository
) {
    fun getById(id: EID) =
        eventRepository.findById(id).getOrNull() ?: throw EventNotFoundException(id)

    fun getByIds(ids: Collection<EID>): List<Event> = eventRepository.findAllById(ids).also { events ->
        if (events.size != ids.size)
            throw EventNotFoundException(diffIdsEntities(ids, events))
    }

    fun findByGroupIdAndUserId(groupId: EID, userId: EID) =
        eventRepository.findByGroupIdAndUsersId(groupId, userId)
}