package pl.edu.agh.timeslotmachine.backend.preference

import org.springframework.data.jpa.repository.*
import org.springframework.stereotype.Repository
import pl.edu.agh.timeslotmachine.backend.core.EID

@Repository
interface PreferenceRepository : JpaRepository<Preference, Preference.Id>, JpaSpecificationExecutor<Preference> {
    @EntityGraph(value = "fetch-all", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Preference p where p.id = :id")
    fun findAndFetchAllById(id: Preference.Id): Preference?

    @Modifying
    @Query(
        "insert into preference values(" +
                ":#{#pref.id.userId}, :#{#pref.id.concreteEventId}," +
                ":#{#pref.points}, :#{#pref.isAssigned}," +
                ":#{#pref.isImpossible}, :#{#pref.impossibilityJustification})",
        nativeQuery = true)
    fun saveIncomplete(pref: Preference)

    fun findByUserId(userId: EID): List<Preference>

    // https://stackoverflow.com/a/15360333/9200394
    @Query("""
        select p from Preference p 
        join fetch p.concreteEvent ce 
        join fetch ce.event e
        where e.group.id = :groupId
    """)
    fun findByGroupId(groupId: EID): List<Preference>

    @Query("""
        select p from Preference p
        join fetch p.concreteEvent ce
        join fetch ce.event e
        where
            e.group.id = :groupId and
            p.user.id = :userId and
            (:isAssigned is null or p.isAssigned = :isAssigned)
    """)
    fun findByGroupIdAndUserId(groupId: EID, userId: EID, isAssigned: Boolean? = null): List<Preference>
}