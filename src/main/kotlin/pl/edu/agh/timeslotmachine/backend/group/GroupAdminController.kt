package pl.edu.agh.timeslotmachine.backend.group

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.*
import pl.edu.agh.timeslotmachine.backend.core.ADMIN_PATH
import pl.edu.agh.timeslotmachine.backend.annotation.SwaggerSessionSecurity
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.exchange.ExchangeService
import pl.edu.agh.timeslotmachine.backend.group.overview.ReassignSummary
import pl.edu.agh.timeslotmachine.backend.user.User
import pl.edu.agh.timeslotmachine.backend.group.webEdit.GroupEditInfo
import pl.edu.agh.timeslotmachine.backend.group.webEdit.GroupWebEditService
import pl.edu.agh.timeslotmachine.backend.group.webEdit.ReassignUserForGroupInfo
import pl.edu.agh.timeslotmachine.backend.importer.GroupImportInfo
import pl.edu.agh.timeslotmachine.backend.importer.ImporterService
import pl.edu.agh.timeslotmachine.backend.user.IndexNo
import pl.edu.agh.timeslotmachine.backend.user.UserService

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("$ADMIN_PATH/groups")
@SwaggerSessionSecurity
class GroupAdminController(
    private val groupCRUDService: GroupCRUDService,
    private val groupScheduleService: GroupScheduleService,
    private val groupWebEditService: GroupWebEditService,
    private val exchangeService: ExchangeService,
    private val userService: UserService,
    private val importerService: ImporterService
) {

    @GetMapping("/")
    fun getAllGroups() =
        groupCRUDService.getAllGroups()

    @PostMapping("/")
    fun createGroup(
        @RequestBody root: GroupRootInfo,
        @RequestAttribute user: User
    ) = run {
        logger.info { root.toString() }
        groupWebEditService.createGroup(root.group)
    }

    @PostMapping("/{groupId}/status")
    fun setStatus(
        @PathVariable groupId: EID,
        @RequestBody statusRoot: GroupStatusInfo,
        @RequestAttribute user: User
    ) = groupCRUDService.setGroupStatusInTransaction(groupId, statusRoot.status)

    @GetMapping("/{groupId}/users")
    fun getGroupUsers(
        @PathVariable groupId: EID,
        @RequestAttribute user: User
    ) = Users(groupCRUDService.getById(groupId).users)

    @GetMapping("/{groupId}/users/{userId}/concretes/overview")
    fun getUserConcreteEvents(
        @PathVariable groupId: EID,
        @PathVariable userId: EID,
        @RequestAttribute user: User
    ) = userService.getConcreteEventsOverview(groupId, userId)

    @DeleteMapping("/{id}")
    fun deleteGroup(@PathVariable id: EID) =
        groupCRUDService.deleteById(id)

    @PutMapping("/{id}")
    fun generateSchedule(
        @PathVariable id: EID
    ) = groupScheduleService.generateSchedule(id)

    @GetMapping("/{groupId}/users/{userId}/reassign")
    fun getEventsForReassign(
        @PathVariable groupId: EID,
        @PathVariable userId: EID,
    ) = exchangeService.getEventsForReassign(groupId, userId)

    @PostMapping("/import")
    fun import(
        @RequestBody groupImportInfo: GroupImportInfo
    ) = importerService.import(groupImportInfo)

    @PostMapping("/user/group/reassign")
    fun assignNewUserForGroup(
        @RequestBody reassignUserForGroupInfo: ReassignUserForGroupInfoRoot
    ) = groupWebEditService.assignUserForGroup(reassignUserForGroupInfo.reassignUser)

    @DeleteMapping("/user/group/reassign")
    fun unassignUserForGroup(
        @RequestBody reassignUserForGroupInfo: ReassignUserForGroupInfoRoot
    ) = groupWebEditService.unassignUserForGroup(reassignUserForGroupInfo.reassignUser)

    @GetMapping("/{groupId}/user/{indexNo}/reassign")
    fun getReassignUserOverview(
        @PathVariable groupId: EID,
        @PathVariable indexNo: IndexNo
    ) = ReassignSummaryRoot(groupWebEditService.getReassignUserOverview(groupId, indexNo))

    data class GroupRootInfo(val group: GroupEditInfo)
    data class GroupStatusInfo(val status: GroupStatus)
    data class Users(val users: Set<User>)
    data class ReassignUserForGroupInfoRoot(val reassignUser: ReassignUserForGroupInfo)
    data class ReassignSummaryRoot(val reassignSummary: ReassignSummary)
}