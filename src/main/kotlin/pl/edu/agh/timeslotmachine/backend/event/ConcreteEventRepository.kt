package pl.edu.agh.timeslotmachine.backend.event

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import pl.edu.agh.timeslotmachine.backend.core.EID

@Repository
interface ConcreteEventRepository : JpaRepository<ConcreteEvent, EID> {

    @Query("""select ce from ConcreteEvent ce 
              join fetch ce.event e
              where e.group.id = :groupId """)
    fun findByGroupId(groupId: EID): List<ConcreteEvent>

    @Query(
        value = """
        select * from concrete_event 
        where event_id = :eventId and activity_form = cast(:#{#activityForm.toString().toLowerCase()} as activity_form)
    """,
        nativeQuery = true
    )
    fun findByEventIdAndActivityForm(eventId: EID, activityForm: ActivityForm): List<ConcreteEvent>
}