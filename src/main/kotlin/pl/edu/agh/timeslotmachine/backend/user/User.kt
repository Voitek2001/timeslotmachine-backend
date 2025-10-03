package pl.edu.agh.timeslotmachine.backend.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObject
import pl.edu.agh.timeslotmachine.backend.event.Event
import pl.edu.agh.timeslotmachine.backend.exchange.Exchange
import pl.edu.agh.timeslotmachine.backend.group.Group

// explicit table naming is required since
// 'user' is a reserved keyword in Postgres,
// so it must be quoted

// note: do not use data class as spring won't
// be able to create a proxy object since
// data classes are final

@Entity
@Table(name = "`user`")
open class User(
    open val username: String,

    @JsonIgnore
    open val passwordHash: String,

    open val name: String,
    open val surname: String,
    open val email: String,
    open val indexNo: IndexNo,

    @Convert(converter = RoleConverter::class)
    open val role: Role,

    @Id
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    override var id: EID? = null,

    // Note: EAGER strategy makes sense (will lead to performance boost)
    // in the following scenarios:
    // 1. Caching: we will cache `user` object for several REST requests, e.g.
    //    `GET /users/$id` and `GET /users/$id/groups` - only 1 database query
    //    should be executed. The hard way.
    // 2. Return groups as part of `GET /users/$id` request. This can be achieved
    //    just by removing `@JsonIgnore`. The easy (and preferable?) way.
    // See also: jackson json views. They can be useful if we want to implement
    // user profiles i.e., users can see each other's profiles.
    // Edit: Most likely (99%) we will use json views. Just not now.
    // TODO: decide between EAGER or LAZY
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "users")
    open var groups: MutableSet<Group> = mutableSetOf(),

    /**
     * Represents ALL events selected by the user from ALL groups.
     * If you want to get a list of events selected by the user
     * in a specific group, use [GroupRepository#findByGroupIdAndUsersId]
     * or [GroupRepository#findByGroupAndUsers]
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    open val events: Set<Event> = setOf(),

    //TODO: consider removing
    @JsonIgnore
    @OneToMany(mappedBy = "exchangeInitiator")
    open val exchanges: MutableSet<Exchange> = mutableSetOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "exchangeAcceptor")
    open val acceptedExchanges: MutableSet<Exchange> = mutableSetOf()

) : EntityObject() {
    //TODO

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id == null || other.id == null)
            return super.equals(other)

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: super.hashCode()
    }
}