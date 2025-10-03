package pl.edu.agh.timeslotmachine.backend.preference

import org.springframework.web.bind.annotation.*
import pl.edu.agh.timeslotmachine.backend.annotation.SwaggerSessionSecurity
import pl.edu.agh.timeslotmachine.backend.core.ADMIN_PATH
import pl.edu.agh.timeslotmachine.backend.user.User

@RestController
@RequestMapping("$ADMIN_PATH/preferences")
@SwaggerSessionSecurity
class PreferenceAdminController(
    private val preferenceService: PreferenceService
) {
    @PostMapping("/reassign/")
    fun reassign(
        @RequestBody root: ReassignPreferenceRequestRoot,
        @RequestAttribute user: User
    ) = preferenceService.reassign(root.reassign)

    data class ReassignPreferenceRequestRoot(
        val reassign: ReassignPreferenceRequest
    )
}