package pl.edu.agh.timeslotmachine.backend.event

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObjectNotFoundException

class ConcreteEventNotFoundException(
    ids: List<EID>
) : EntityObjectNotFoundException(ConcreteEvent::class, ids) {
    constructor(id: EID) : this(listOf(id))
}