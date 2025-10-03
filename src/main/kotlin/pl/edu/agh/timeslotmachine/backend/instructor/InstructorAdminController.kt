package pl.edu.agh.timeslotmachine.backend.instructor

import org.springframework.web.bind.annotation.*
import pl.edu.agh.timeslotmachine.backend.core.ADMIN_PATH
import pl.edu.agh.timeslotmachine.backend.annotation.SwaggerSessionSecurity
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.user.User

@RestController
@RequestMapping("$ADMIN_PATH/instructors")
@SwaggerSessionSecurity
class InstructorAdminController(
    private val instructorService: InstructorService
) {
    @GetMapping("/")
    fun getAllInstructors() =
        instructorService.getAllInstructors()

    @GetMapping("/{id}/")
    fun getInstructor(@PathVariable id: EID) =
        instructorService.getById(id)

    @PutMapping("/")
    fun updateInstructor(
        @RequestBody root: InstructorRootInfo
    ) = instructorService.updateInstructorById(root.instructor)

    @PostMapping("/")
    fun createGroup(
        @RequestBody root: InstructorRootInfo,
        @RequestAttribute user: User
    ) = instructorService.createInstructor(root.instructor)

    @DeleteMapping("/{id}")
    fun deleteGroup(@PathVariable id: EID) =
        instructorService.deleteById(id)

    data class InstructorRootInfo(val instructor: InstructorInfo)
}