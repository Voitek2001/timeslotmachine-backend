package pl.edu.agh.timeslotmachine.backend.event

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.group.Group
import pl.edu.agh.timeslotmachine.backend.user.User

@Repository
interface EventRepository : JpaRepository<Event, EID> {
    // ignore IntelliJ warning - possible a bug in IntelliJ
    fun findByGroupAndUsers(group: Group, user: User): List<Event>

    fun findByGroupIdAndUsersId(groupId: EID, userId: EID): List<Event>
    fun findByGroupId(groupId: EID): List<Event>
}