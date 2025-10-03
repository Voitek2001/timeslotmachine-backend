package pl.edu.agh.timeslotmachine.backend.place

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.ColumnTransformer
import org.springframework.data.geo.Point
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObject
import pl.edu.agh.timeslotmachine.backend.core.converter.PointConverter

@Entity
open class Place(
    open var name: String,
    open var description: String,

    // null can mean "online"
    @Convert(converter = PointConverter::class)
    @ColumnTransformer(write = "?::point")
    open var localization: Point?,

    open var room: String,

    @Id
    @SequenceGenerator(name = "place_id_seq", sequenceName = "place_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_id_seq")
    override var id: EID? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "place")
    open val concreteEvents: Set<ConcreteEvent> = emptySet()
) : EntityObject()