package pl.edu.agh.timeslotmachine.backend.group

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.timeslotmachine.backend.checker.Checker
import pl.edu.agh.timeslotmachine.backend.checker.GroupSatisfies
import pl.edu.agh.timeslotmachine.backend.checker.then
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.solver.SolverService

@Service
class GroupScheduleService(
    private val groupService: GroupCRUDService,
    private val solverService: SolverService
) : Checker() {
    @Transactional
    fun generateSchedule(groupId: EID) = require(
        GroupSatisfies(groupId) { it.status != GroupStatus.Busy }
    ).then {
        groupService.setGroupStatus(groupId, GroupStatus.Busy)

        Thread {
            try {
                solverService.solveFor(groupId) {
                    groupService.setGroupStatus(groupId, GroupStatus.Ready)
                }
            } catch (ex: Exception) {
                groupService.setGroupStatusInTransaction(groupId, GroupStatus.Closed)
                throw ex
            }
        }.start()
    }
}