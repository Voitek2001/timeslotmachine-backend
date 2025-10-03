package pl.edu.agh.timeslotmachine.backend.instructor

import pl.edu.agh.timeslotmachine.backend.core.EID


data class InstructorInfo(
    val id: EID? = null,
    val fullName: String? = null
)