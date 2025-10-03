package pl.edu.agh.timeslotmachine.backend.group

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonView
import jakarta.persistence.*
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObject
import pl.edu.agh.timeslotmachine.backend.event.Event
import pl.edu.agh.timeslotmachine.backend.user.User
import pl.edu.agh.timeslotmachine.backend.core.View

// explicit table naming is required since
// 'group' is a reserved keyword in Postgres,
// so it must be quoted

// note: Getter methods of lazy classes cannot be final
// note: proxies can be created only for open classes

@Entity
@Table(name = "`group`")
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
open class Group(
    open val name: String,
    open val description: String,

    /**
     * The global limit of max points per concrete
     * event for the whole group (semester)
     */
    open val maxPointsPerConcrete: Int,

    @Id
    @SequenceGenerator(name = "group_id_seq", sequenceName = "group_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_id_seq")
    override var id: EID? = null,

    @JsonIgnore
    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "users_groups",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    open var users: MutableSet<User> = mutableSetOf(),

    @JsonView(IncludeEvents::class)
    @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL])
    open var events: MutableSet<Event> = mutableSetOf(),

    @Convert(converter = GroupStatusConverter::class)
    open var status: GroupStatus = GroupStatus.Open
) : EntityObject() {
    interface IncludeEvents : View.Public
}