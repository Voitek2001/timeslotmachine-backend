package pl.edu.agh.timeslotmachine.backend.group.overview

import pl.edu.agh.timeslotmachine.backend.group.Group
import pl.edu.agh.timeslotmachine.backend.user.User

data class ReassignSummary(
    val group: Group,
    val user: User
)
