package pl.edu.agh.timeslotmachine.backend.place

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.timeslotmachine.backend.core.EID

@Repository
interface PlaceRepository : JpaRepository<Place, EID> {
    fun findByNameAndRoom(name: String, room: String): Place?
}