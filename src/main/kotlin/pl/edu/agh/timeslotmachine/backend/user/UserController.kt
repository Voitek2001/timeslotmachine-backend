package pl.edu.agh.timeslotmachine.backend.user

import com.fasterxml.jackson.annotation.JsonView
import org.springframework.web.bind.annotation.*
import pl.edu.agh.timeslotmachine.backend.annotation.SwaggerSessionSecurity
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.group.Group
import pl.edu.agh.timeslotmachine.backend.core.IdObject
import pl.edu.agh.timeslotmachine.backend.preference.PreferenceInfo

@RestController
@RequestMapping("/users")
@SwaggerSessionSecurity
class UserController(
    private val userService: UserService
) {
    @GetMapping("/self/")
    fun getSelf(
        @RequestAttribute user: User
    ) = SelfResponse(user)

    @GetMapping("/self/groups")
    fun getGroups(
        @RequestAttribute user: User
    ) = GroupsResponse(user.groups)

    @PutMapping("/self/events")
    fun putEvents(
        @RequestBody eventEnrollment: EventEnrollment,
        @RequestAttribute user: User
    ) = userService.addEvents(user.id!!, eventEnrollment.toIds())

    @PostMapping("/self/groups/{groupId}/events")
    fun setEvents(
        @PathVariable groupId: EID,
        @RequestBody eventEnrollment: EventEnrollment,
        @RequestAttribute user: User
    ) = userService.setEvents(user.id!!, groupId, eventEnrollment.toIds())

    /**
     * Modifies the specified preferences.
     * If the preference does not exist,
     * it will be created.
     */
    @PutMapping("/self/preferences")
    fun putPreferences(
        @RequestBody preferenceRootInfo: PreferenceRootInfo,
        @RequestAttribute user: User
    ) = userService.putPreferences(user, preferenceRootInfo.preferences)

    data class EventEnrollment(
        val events: List<IdObject>
    ) {
        fun toIds() = events.map { it.id }
    }

    data class PreferenceRootInfo(
        val preferences: List<PreferenceInfo>
    )

    data class SelfResponse(
        val user: User,
        val success: Boolean = true
    )

    data class GroupsResponse(
        val groups: Set<Group>
    )
}