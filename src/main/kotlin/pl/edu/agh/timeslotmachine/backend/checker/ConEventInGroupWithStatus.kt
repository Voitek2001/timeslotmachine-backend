package pl.edu.agh.timeslotmachine.backend.checker

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.group.GroupCRUDService
import pl.edu.agh.timeslotmachine.backend.group.GroupStatus

class ConEventInGroupWithStatus (
    private val conEventIds: List<EID>,
    private val status: GroupStatus
) : Check {

    constructor(conEventId: EID, status: GroupStatus) : this(listOf(conEventId), status)

    override fun invoke(context: CheckerContext) = conEventIds.all { conEventId ->
        context.bean<GroupCRUDService>().getByConEventId(conEventId).let {
            it.status == status
        }
    }
}