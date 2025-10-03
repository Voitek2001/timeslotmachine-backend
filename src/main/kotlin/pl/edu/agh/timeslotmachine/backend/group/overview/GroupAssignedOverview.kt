package pl.edu.agh.timeslotmachine.backend.group.overview

data class GroupAssignedOverview(
    val group: GroupSummary,
    val week: List<WeekDay>
)