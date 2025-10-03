package pl.edu.agh.timeslotmachine.backend.group

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.core.EntityObjectNotFoundException

class GroupNotFoundException private constructor(
    message: String
) : EntityObjectNotFoundException(message) {
    constructor(id: EID) : this(
        "The specified group (id=$id) cannot be found"
    )

    constructor(id: EID, userId: EID) : this(
        "The group (id=$id) cannot be found or the user (id=$userId) does not belong to the specified group"
    )
}