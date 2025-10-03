package pl.edu.agh.timeslotmachine.backend.checker

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.group.Group
import pl.edu.agh.timeslotmachine.backend.group.GroupCRUDService

open class GroupSatisfies(
    private val groupId: EID,
    private val pred: (Group) -> Boolean
) : Check {
    override fun invoke(context: CheckerContext) = context.storage.storedOrCreate<Group>(groupId) {
        context.bean<GroupCRUDService>().getById(groupId)
    }.let { pred(it) }
}