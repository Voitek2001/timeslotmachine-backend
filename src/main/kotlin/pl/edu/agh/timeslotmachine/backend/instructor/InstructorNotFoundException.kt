package pl.edu.agh.timeslotmachine.backend.instructor

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObjectNotFoundException

class InstructorNotFoundException(
    id: EID
) : EntityObjectNotFoundException(Instructor::class, listOf(id))