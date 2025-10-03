package pl.edu.agh.timeslotmachine.backend.event

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import pl.edu.agh.timeslotmachine.backend.annotation.DebugRestController
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.group.GroupRepository
import pl.edu.agh.timeslotmachine.backend.instructor.InstructorRepository
import pl.edu.agh.timeslotmachine.backend.place.PlaceRepository
import pl.edu.agh.timeslotmachine.backend.term.Term
import pl.edu.agh.timeslotmachine.backend.user.UserRepository
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@DebugRestController
class EventDebugController(
    private val eventRepository: EventRepository,
    private val concreteEventRepository: ConcreteEventRepository,
    private val placeRepository: PlaceRepository,
    private val instructorRepository: InstructorRepository,
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) {
    init {
        logger.debug { "init" }
    }

    @GetMapping("/events")
    fun getEvents() = run {
        val events = eventRepository.findAll()
        logger.debug { events }
        events.asSequence()
    }

    @GetMapping("/events/{id}/concretes")
    fun getConcretes(
        @PathVariable id: EID
    ) = run {
        val event = eventRepository.findById(id).orElseThrow()
        logger.debug { event }
        event.concreteEvents
    }

    @GetMapping("/concretes/{id}")
    fun getConcrete(
        @PathVariable id: EID
    ) = run {
        val event = concreteEventRepository.findById(id).orElseThrow()
        event.event
    }

    data class ConcreteEventDetails(
        val placeId: EID,
        val userLimit: Int,
        val terms: List<TermDetails>
    ) {
        data class TermDetails(
            val start: LocalDateTime,
            val end: LocalDateTime,
            val instructorId: EID
        )
    }

    @PostMapping("/events/{id}/concretes")
    fun addConcrete(
        @PathVariable id: EID,
        @RequestBody details: ConcreteEventDetails
    ) = eventRepository.findById(id).orElseThrow().run {
        val concreteEvent = ConcreteEvent(
            place = placeRepository.findById(details.placeId).orElseThrow(),
            userLimit = details.userLimit,
            activityForm = ActivityForm.Classes,
            event = this
        )
        concreteEvents += concreteEvent
        eventRepository.save(this)
        concreteEvent.terms += details.terms.map {
            Term(
                start = it.start,
                end = it.end,
                concreteEvent = concreteEvent,
                instructor = instructorRepository.findById(it.instructorId).orElseThrow()
            )
        }
        concreteEventRepository.save(concreteEvent)
    }

    @GetMapping("/events/find")
    fun getByGroupAndUser(
        @RequestParam groupId: EID,
        @RequestParam userId: EID
    ) = eventRepository.findByGroupIdAndUsersId(groupId, userId)
}