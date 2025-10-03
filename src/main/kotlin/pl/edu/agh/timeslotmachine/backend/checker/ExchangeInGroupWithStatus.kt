package pl.edu.agh.timeslotmachine.backend.checker

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.group.GroupCRUDService
import pl.edu.agh.timeslotmachine.backend.group.GroupStatus

class ExchangeInGroupWithStatus(
    private val exchangeId: EID,
    private val groupStatus: GroupStatus
) : Check {
    override fun invoke(context: CheckerContext) = context.bean<GroupCRUDService>().getByExchangeId(
        exchangeId
    ).let {
        it.status == groupStatus
    }
}