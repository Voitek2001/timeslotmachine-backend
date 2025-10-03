package pl.edu.agh.timeslotmachine.backend.checker

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.group.GroupCRUDService

class UserInGroup(
    private val userId: EID,
    private val groupId: EID
) : Check {
    override fun invoke(context: CheckerContext) =
        context.bean<GroupCRUDService>().containsUser(groupId, userId)
}