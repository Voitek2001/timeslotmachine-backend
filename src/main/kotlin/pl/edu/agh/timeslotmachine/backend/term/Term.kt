package pl.edu.agh.timeslotmachine.backend.term

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.*
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.instructor.Instructor
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObject
import java.time.LocalDateTime

@Entity
open class Term(

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    open val start: LocalDateTime,

    // end is a reserved keyword, so it must be quoted
    @Column(name = "`end`")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    open val end: LocalDateTime,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concrete_event_id")
    open val concreteEvent: ConcreteEvent,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    open val instructor: Instructor,

    @Id
    @SequenceGenerator(name = "term_id_seq", sequenceName = "term_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "term_id_seq")
    override var id: EID? = null
) : EntityObject() {

    fun isOverlap(other: Term): Boolean =
        start.isBefore(other.end) && end.isAfter(other.start)

    override fun toString(): String {
        return "Term(start=$start, end=$end)"
    }

}