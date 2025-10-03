package pl.edu.agh.timeslotmachine.backend.preference

import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.event.Event
import pl.edu.agh.timeslotmachine.backend.group.Group

class PreferenceSpecification {

    fun getPreferenceByGroupId(groupId: Long): Specification<Preference> {
        return Specification { root, query, criteriaBuilder ->
            // Dołączamy tabelę concrete_event do preference
            val concreteEventJoin = root.join<Preference, ConcreteEvent>("concreteEvent", JoinType.INNER)

            // Dołączamy tabelę event do concrete_event
            val eventJoin = concreteEventJoin.join<ConcreteEvent, Event>("event", JoinType.INNER)

            // Dołączamy tabelę group do event
            val groupJoin = eventJoin.join<Event, Group>("group", JoinType.INNER)

            // Dodajemy kryterium filtrowania po groupId
            criteriaBuilder.equal(groupJoin.get<Long>("id"), groupId)
        }
    }
}