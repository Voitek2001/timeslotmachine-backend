package pl.edu.agh.timeslotmachine.backend.importer

data class GroupImportInfo(
    val name: String,
    val description: String,
    val maxPointsPerConcrete: Int,
    val events: Set<EventImportInfo>,
    val assignedUsers: Set<AssignedUserImportInfo>
)
