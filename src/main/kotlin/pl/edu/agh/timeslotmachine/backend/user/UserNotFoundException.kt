package pl.edu.agh.timeslotmachine.backend.user

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObjectNotFoundException

class UserNotFoundException(
    id: EID
) : EntityObjectNotFoundException(User::class, listOf(id))