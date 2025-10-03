package pl.edu.agh.timeslotmachine.backend.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import pl.edu.agh.timeslotmachine.backend.core.EID
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, EID> {
    fun findByUsernameAndPasswordHash(username: String, passwordHash: String): User?

    fun existsByUsernameOrEmail(username: String, email: String): Boolean

    /**
     * @param eventIds The parameter should be of type Array, not List.
     */
    @Modifying
    @Query("""
        insert into users_events (user_id, event_id) 
        select :userId, e.id from unnest(:eventIds) AS e(id)
        on conflict do nothing
    """, nativeQuery = true)
    fun addEvents(userId: EID, eventIds: Array<EID>)

    @Modifying
    @Query("""
        delete from users_events
        where user_id = :userId and event_id in (
            select id from event
            where group_id = :groupId
        );
        insert into users_events (user_id, event_id) 
        select :userId, e.id from unnest(:eventIds) AS e(id)
        on conflict do nothing
    """, nativeQuery = true)
    fun setGroupEvents(userId: EID, groupId: EID, eventIds: Array<EID>)

    fun findByUsername(username: String): User

    @Query("select u from User u where u.indexNo = :indexNo")
    fun findUserByIndexNo(indexNo: IndexNo): Optional<User>
}