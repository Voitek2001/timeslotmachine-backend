package pl.edu.agh.timeslotmachine.backend.event

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import org.hibernate.annotations.ColumnTransformer
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObject
import pl.edu.agh.timeslotmachine.backend.exchange.Exchange
import pl.edu.agh.timeslotmachine.backend.place.Place
import pl.edu.agh.timeslotmachine.backend.preference.Preference
import pl.edu.agh.timeslotmachine.backend.term.Term

@Entity
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
open class ConcreteEvent(
    @ManyToOne
    @JoinColumn(name = "place_id")
    open val place: Place,

    open val userLimit: Int,

    @Convert(converter = ActivityFormConverter::class)
    @ColumnTransformer(write = "?::activity_form")
    open val activityForm: ActivityForm,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    open val event: Event,

    @JsonIgnore
    @OneToMany(mappedBy = "concreteEvent", cascade = [CascadeType.ALL])
    open var terms: MutableSet<Term> = mutableSetOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "concreteEvent", cascade = [CascadeType.ALL])
    open var preferences: MutableSet<Preference> = mutableSetOf(),

    //TODO: consider to remove
    @JsonIgnore
    @OneToMany(mappedBy = "conEventOffered")
    open val exchangesOffered: MutableSet<Exchange> = mutableSetOf(),

    //TODO: consider to remove
    @JsonIgnore
    @OneToMany(mappedBy = "conEventRequested")
    open val exchangesRequested: MutableSet<Exchange> = mutableSetOf(),

    @Id
    @SequenceGenerator(name = "concrete_event_id_seq", sequenceName = "concrete_event_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "concrete_event_id_seq")
    override var id: EID? = null
) : EntityObject() {

    fun isConflict(other: ConcreteEvent): Boolean {
        // TODO: zip
        for (term in terms) {
            for (otherTerm in other.terms) {
                if (term.isOverlap(otherTerm)) return true
            }
        }
        return false
    }

    override fun toString(): String {
        return "ConcreteEvent(terms=$terms, event=$event, userLimit=$userLimit)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConcreteEvent

        if (id == null || other.id == null)
            return super.equals(other)

        return id == other.id // TODO
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: super.hashCode()
    }

}