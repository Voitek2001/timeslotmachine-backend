package pl.edu.agh.timeslotmachine.backend.group.overview

data class WeekDay(
    val day: Int, // 1-7, where 1 - monday
    val slots: List<DaySlot>
)