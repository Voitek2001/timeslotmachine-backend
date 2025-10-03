package pl.edu.agh.timeslotmachine.backend.preference

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.GeneralEntityObject
import pl.edu.agh.timeslotmachine.backend.user.User

@NamedEntityGraph(
    name = "fetch-all", // TODO: MV
    attributeNodes = [
        NamedAttributeNode("user"),
        NamedAttributeNode("concreteEvent"),
    ]
)
@Entity
open class Preference(
    @EmbeddedId
    override var id: Id,

    open var points: Int,
    open var isAssigned: Boolean,

    open var isImpossible: Boolean = false,
    open var impossibilityJustification: String? = null
) : GeneralEntityObject<Preference.Id>() {
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    open var user: User? = null
        get() = field ?: notPersistedError() // TODO

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("concreteEventId")
    open var concreteEvent: ConcreteEvent? = null
        get() = field ?: notPersistedError()

    private fun notPersistedError(): Nothing =
        throw RuntimeException("Invalid state: this field can only be read from persisted object")

    @Embeddable
    data class Id(
        val userId: EID,
        val concreteEventId: EID
    )

    constructor(
        user: User,
        concreteEvent: ConcreteEvent,
        points: Int,
        isAssigned: Boolean,
        isImpossible: Boolean = false,
        impossibilityJustification: String? = null
    ) : this(
        id = Id(user.id!!, concreteEvent.id!!),
        points = points,
        isAssigned = isAssigned,
        isImpossible = isImpossible,
        impossibilityJustification = impossibilityJustification
    ) {
        this.user = user
        this.concreteEvent = concreteEvent
    }
}