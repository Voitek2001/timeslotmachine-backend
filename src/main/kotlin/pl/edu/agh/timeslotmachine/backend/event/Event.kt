package pl.edu.agh.timeslotmachine.backend.event

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jakarta.persistence.*
import org.hibernate.annotations.ColumnTransformer
import pl.edu.agh.timeslotmachine.backend.group.Group
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObject
import pl.edu.agh.timeslotmachine.backend.user.User

// Since Event is lazy loaded in e.x. ConcreteEvent,
// Hibernate will create a proxy object with additional fields.
// These fields should be ignored during serialization.
// See: https://stackoverflow.com/q/52656517/9200394
@Entity
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
open class Event(
    open val name: String,
    open val shortName: String,
    open val description: String,

    // hex value: '#' is omitted
    // TODO: most likely it will be a better idea to create
    //  class named Color to avoid ambiguity
    open val color: String,

    @Convert(converter = PointLimitsConverter::class)
    @JsonSerialize(using = PointLimitsSerializer::class)
    @ColumnTransformer(write = "?::jsonb")
    open val pointLimits: PointLimits,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    open val group: Group,

    @Id
    @SequenceGenerator(name = "event_id_seq", sequenceName = "event_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_id_seq")
    override var id: EID? = null,

    @JsonIgnore
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "event")
    open var concreteEvents: MutableSet<ConcreteEvent> = mutableSetOf(),

    @JsonIgnore
    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "users_events",
        joinColumns = [JoinColumn(name = "event_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    open var users: MutableSet<User> = mutableSetOf()
) : EntityObject() {

    data class ActivityFormLimits(val minPointsPerActivity: Int)

    override fun toString(): String {
        return "Event(name='$name')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        if (id == null || other.id == null)
            return super.equals(other)

        return id == other.id // TODO
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: super.hashCode()
    }
}