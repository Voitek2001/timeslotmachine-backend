package pl.edu.agh.timeslotmachine.backend.checker

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.preference.PreferenceService

class UserHasPreferenceAssigned(
    private val conEventId: EID,
    private val userId: EID
) : Check {
    override fun invoke(context: CheckerContext) = context.bean<PreferenceService>().findById(
        userId, conEventId
    )?.isAssigned == true
}
