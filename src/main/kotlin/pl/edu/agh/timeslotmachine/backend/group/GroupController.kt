package pl.edu.agh.timeslotmachine.backend.group

import com.fasterxml.jackson.annotation.JsonView
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.agh.timeslotmachine.backend.annotation.SwaggerSessionSecurity
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.event.Event
import pl.edu.agh.timeslotmachine.backend.exporter.ExporterService
import pl.edu.agh.timeslotmachine.backend.exporter.ExporterType
import pl.edu.agh.timeslotmachine.backend.group.overview.GroupOverviewService
import pl.edu.agh.timeslotmachine.backend.user.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/groups")
@SwaggerSessionSecurity
class GroupController(
    private val groupOverviewService: GroupOverviewService,
    private val groupCRUDService: GroupCRUDService,
    private val exporterService: ExporterService
) {
    @GetMapping("/")
    fun getUserGroups(
        @RequestAttribute user: User
    ) = UserGroups(groupOverviewService.getUserGroups(user))

    @GetMapping("/{groupId}/events")
    fun getGroupEvents(
        @PathVariable groupId: EID,
        @RequestAttribute user: User
    ) = GroupEvents(groupCRUDService.getById(groupId, user.id!!).events)

    @JsonView(Group.IncludeEvents::class)
    @GetMapping("/{groupId}/overview")
    fun getOverview(
        @PathVariable groupId: EID,
        @RequestAttribute user: User
    ) = groupOverviewService.getOverview(groupId, user.id!!)

    @JsonView(Group.IncludeEvents::class)
    @GetMapping("/{groupId}/overview/assigned")
    fun getAssignedOverview(
        @PathVariable groupId: EID,
        @RequestAttribute user: User
    ) = groupOverviewService.getAssignedOverview(groupId, user.id!!)

    @GetMapping("/{groupId}/export")
    fun export(
        @PathVariable groupId: EID,
        @RequestParam type: ExporterType,
        @RequestAttribute user: User
    ) = exporterService.export(user.id!!, groupId, type).let { exported ->
        ResponseEntity.ok().headers(
            HttpHeaders().apply {
                contentType = exported.type
                contentDisposition = ContentDisposition.builder("attachment").filename(exported.name).build()
            }
        ).body(
            exported.data.toString()
        )
    }

    data class UserGroups(val groups: Set<Group>)
    data class GroupEvents(val events: Set<Event>)
}