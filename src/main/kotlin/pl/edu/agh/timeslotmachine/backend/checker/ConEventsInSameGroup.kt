package pl.edu.agh.timeslotmachine.backend.checker

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.group.GroupCRUDService

class ConEventsInSameGroup(
    private val offeredConEventId: EID,
    private val requestedConEventIds: List<EID>
) : Check {
    override fun invoke(context: CheckerContext) = context.bean<GroupCRUDService>().getByConEventId(
        offeredConEventId
    ).let { offeredGroup ->
        requestedConEventIds.all {
            context.bean<GroupCRUDService>().getByConEventId(it).let { requestedGroup ->
                offeredGroup == requestedGroup
            }
        }
    }
}