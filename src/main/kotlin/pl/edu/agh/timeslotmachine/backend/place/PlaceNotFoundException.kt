package pl.edu.agh.timeslotmachine.backend.place

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObjectNotFoundException

class PlaceNotFoundException(
    id: EID
) : EntityObjectNotFoundException(Place::class, listOf(id))