package pl.edu.agh.timeslotmachine.backend.exchange

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.user.User
import java.util.*


interface ExchangeRepository : JpaRepository<Exchange, EID> {

    @Query("""
        select case when count(ex) > 0 then true else false end from Exchange ex 
        where ex.conEventOffered.id = :offeredId 
            and ex.status = 'pending'
            and ex.isPrivate = false
    """)
    fun existsByConEventOfferedId(offeredId: EID): Boolean


    fun findAllByConEventOfferedIdAndExchangeInitiatorId(conEventId: EID, exchangeInitiatorId: EID): List<Exchange>

    @Query("""
        select ex from Exchange ex 
        join User u1 on ex.exchangeInitiator.id = u1.id
        left join User u2 on ex.exchangeAcceptor.id = u2.id
        join ConcreteEvent ce on ex.conEventRequested.id = ce.id
        join Event e on ce.event.id = e.id
        join Group g on e.group.id = g.id
        where g.id = :groupId 
        and (u1.id = :userId or u2.id = :userId)
    """)
    fun getByInitiatorOrAcceptorAndGroupId(userId: EID, groupId: EID): List<Exchange>

    @Modifying
    @Query("update Exchange e set e.status = :status where e.id = :exchangeId")
    fun setExchangeStatus(exchangeId: EID, status: ExchangeStatus)

    @Modifying
    @Query("update Exchange e set e.exchangeAcceptor = :user where e.id = :exchangeId")
    fun setExchangeAcceptor(exchangeId: EID, user: User)

    @Query("select e.status from Exchange e where e.id = :exchangeId")
    fun getStatus(exchangeId: EID): ExchangeStatus


    @Query("""
        select case when count(ex) > 0 then true else false end from Exchange ex 
        where ex.conEventRequested.id = :requestedId
            and ex.conEventOffered.id = :offeredId 
            and ex.exchangeInitiator = :user
            and ex.status = 'pending'
    """)
    fun existsByOfferedAndRequestedAndInitiator(offeredId: EID, requestedId: EID, user: User): Boolean

    @Query("select ex from Exchange ex where ex.exchangeIdentifier = :exchangeIdentifier")
    fun getByExchangeIdentifier(exchangeIdentifier: UUID): Exchange?

    @Query("""
        select ex from Exchange ex 
            where ex.exchangeAcceptor is null
            and ex.status = 'pending'
            and ex.isPrivate = false
            and ex.conEventRequested.id = :conEventId
    """)
    fun findNotAcceptedAndPendingAndNotPrivate(conEventId: EID): List<Exchange>
}