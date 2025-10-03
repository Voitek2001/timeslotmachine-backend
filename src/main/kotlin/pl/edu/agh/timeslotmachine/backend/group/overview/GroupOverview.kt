package pl.edu.agh.timeslotmachine.backend.group.overview

data class GroupOverview(
    val group: GroupSummary,
    val week: List<WeekDay>,
    val preferences: List<PreferenceSummary>
)
