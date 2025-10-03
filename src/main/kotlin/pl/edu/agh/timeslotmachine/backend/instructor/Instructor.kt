package pl.edu.agh.timeslotmachine.backend.instructor

import jakarta.persistence.*
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObject

@Entity
open class Instructor(
    open var fullName: String,

    @Id
    @SequenceGenerator(name = "instructor_id_seq", sequenceName = "instructor_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instructor_id_seq")
    override var id: EID? = null
) : EntityObject()