package pl.edu.agh.timeslotmachine.backend.group.webEdit

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.user.IndexNo

data class ReassignUserForGroupInfo(
    val userIndexNo: IndexNo,
    val groupId: EID
)
