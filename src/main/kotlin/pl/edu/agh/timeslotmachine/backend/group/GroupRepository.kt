package pl.edu.agh.timeslotmachine.backend.group

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.user.IndexNo

@Repository
interface GroupRepository : JpaRepository<Group, EID> {
    fun findGroupsByUsersId(userId: EID): List<Group>

    // TODO: doc
    fun findGroupByIdAndUsersId(groupId: EID, userId: EID): Group?

    @Query("select g.status from Group g where g.id = :groupId")
    fun getGroupStatus(groupId: EID): GroupStatus

    @Modifying
    @Query("update Group g set g.status = :status where g.id = :groupId")
    fun setGroupStatus(groupId: EID, status: GroupStatus)

    @Modifying
    @Query(
        "insert into users_groups (user_id, group_id)\n" +
        "values ((select id from \"user\" where index_no = :indexNo), :groupId)",
        nativeQuery = true
    )
    fun addUserByIndexNo(groupId: EID, indexNo: IndexNo)

    @Query(
        "select case when count(*) = 1 then true else false end " +
        "from Group g left join g.users u " +
        "where g.id = :groupId and u.id = :userId"
    )
    fun containsUser(groupId: EID, userId: EID): Boolean

    @Query("""
        select g from Group g 
            join Event e on e.group.id = g.id
            join ConcreteEvent ce on ce.event.id = e.id
            join Exchange ex on ex.conEventRequested.id = ce.id
            where ex.id = :exchangeId
    """)
    fun getByExchangeId(exchangeId: EID): Group

    @Query("""
        select g from Group g
        join Event e on e.group.id = g.id
        join ConcreteEvent ce on ce.event.id = e.id
        where ce.id = :conEventId
    """)
    fun getByConEventId(conEventId: EID): Group

    fun getAllByIdIn(ids: Collection<Long>): Set<Group>

    @Modifying
    @Query("delete from users_groups " +
            "where group_id = :groupId" +
            " and user_id = (select id from \"user\" where index_no = :indexNo)",
        nativeQuery = true)
    fun removeUserGroupByIndexNo(groupId: EID, indexNo: IndexNo)

    @Query(
        "SELECT CASE WHEN COUNT(*) = 1 THEN TRUE ELSE FALSE END " +
                "FROM users_groups ug " +
                "JOIN \"user\" u ON ug.user_id = u.id " +
                "WHERE ug.group_id = :groupId AND u.index_no = :indexNo",
        nativeQuery = true
    )
    fun containsUserByIndexNo(groupId: EID, indexNo: IndexNo): Boolean
}