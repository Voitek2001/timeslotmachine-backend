package pl.edu.agh.timeslotmachine.backend.event

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObjectNotFoundException

class EventNotFoundException(
    ids: List<EID>
) : EntityObjectNotFoundException(Event::class, ids) {
    constructor(id: EID) : this(listOf(id))
}