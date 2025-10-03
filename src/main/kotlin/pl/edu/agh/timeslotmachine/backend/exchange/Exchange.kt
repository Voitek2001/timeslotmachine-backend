package pl.edu.agh.timeslotmachine.backend.exchange

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.TraceEntityObject
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.user.User
import java.util.UUID

@Entity
@Table(name = "exchange")
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
open class Exchange(

    @Id
    @SequenceGenerator(name = "exchange_id_seq", sequenceName = "exchange_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_id_seq")
    override var id: EID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    open val exchangeInitiator: User,

    @ManyToOne(fetch = FetchType.LAZY)
    open val conEventOffered: ConcreteEvent,

    @ManyToOne(fetch = FetchType.LAZY)
    open val conEventRequested: ConcreteEvent,

    @Convert(converter = ExchangeStatusConverter::class)
    open var status: ExchangeStatus = ExchangeStatus.Pending,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "accepted_exchange",
        joinColumns = [JoinColumn(name = "exchange_id")],
        inverseJoinColumns = [JoinColumn(name = "exchange_acceptor_id")]
    )
    open var exchangeAcceptor: User? = null,

    open var isPrivate: Boolean,

    open var exchangeIdentifier: UUID

) : TraceEntityObject()
