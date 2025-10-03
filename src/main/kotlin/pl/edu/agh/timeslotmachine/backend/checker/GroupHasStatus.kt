package pl.edu.agh.timeslotmachine.backend.checker

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.group.GroupStatus

class GroupHasStatus(
    groupId: EID,
    status: GroupStatus
) : GroupSatisfies(groupId, { it.status == status })