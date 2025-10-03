package pl.edu.agh.timeslotmachine.backend.importer

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.event.Event
import pl.edu.agh.timeslotmachine.backend.group.Group
import pl.edu.agh.timeslotmachine.backend.group.GroupCRUDService
import pl.edu.agh.timeslotmachine.backend.group.GroupStatus
import pl.edu.agh.timeslotmachine.backend.instructor.InstructorInfo
import pl.edu.agh.timeslotmachine.backend.instructor.InstructorService
import pl.edu.agh.timeslotmachine.backend.place.PlaceInfo
import pl.edu.agh.timeslotmachine.backend.place.PlaceService
import pl.edu.agh.timeslotmachine.backend.term.Term

@Service
class ImporterService(
    private val groupCRUDService: GroupCRUDService,
    private val placeService: PlaceService,
    private val instructorService: InstructorService
) {
    @Transactional
    fun import(info: GroupImportInfo) =
        Importer(info).import()

    private fun getOrCreatePlaces(info: GroupImportInfo) = info.events.asSequence().flatMap {
        it.concreteEvents
    }.mapTo(HashSet()) {
        it.place
    }.associateWith {
        placeService.getByNameAndRoomOrCreate(PlaceInfo(
            name = it.name,
            description = it.description,
            localization = it.localization,
            room = it.room
        ))
    }

    private fun getOrCreateInstructors(info: GroupImportInfo) = info.events.asSequence().flatMap {
        it.concreteEvents
    }.flatMap {
        it.terms
    }.mapTo(HashSet()) {
        it.instructor
    }.associateWith {
        instructorService.getByFullNameOrCreate(InstructorInfo(
            fullName = it.fullName
        ))
    }

    private inner class Importer(
        private val info: GroupImportInfo
    ) {
        private val places = getOrCreatePlaces(info)
        private val instructors = getOrCreateInstructors(info)

        fun import() = instantiateGroup().let {
            groupCRUDService.save(it)
        }.let { group ->
            info.assignedUsers.forEach {
                groupCRUDService.addUserByIndexNo(group.id!!, it.indexNo)
            }
        }

        private fun instantiateGroup() = Group(
            name = info.name,
            description = info.description,
            maxPointsPerConcrete = info.maxPointsPerConcrete,
        ).apply {
            events += instantiateEvents(info.events)
            status = GroupStatus.Open
        }

        private fun Group.instantiateEvents(
            importInfo: Set<EventImportInfo>
        ) = importInfo.map {
            Event(
                name = it.name,
                shortName = it.shortName,
                description = it.description,
                color = it.color,
                pointLimits = it.pointLimits,
                group = this
            ).apply {
                concreteEvents += instantiateConcreteEvents(it.concreteEvents)
            }
        }

        private fun Event.instantiateConcreteEvents(importInfo: Set<ConcreteEventImportInfo>) = importInfo.map {
            ConcreteEvent(
                place = places[it.place]!!,
                userLimit = it.limit,
                activityForm = it.activityForm,
                event = this
            ).apply {
                terms += instantiateTerms(it.terms)
            }
        }

        private fun ConcreteEvent.instantiateTerms(importInfo: Set<TermImportInfo>) = importInfo.map {
            Term(
                start = it.start,
                end = it.end,
                concreteEvent = this,
                instructor = instructors[it.instructor]!!
            )
        }
    }
}