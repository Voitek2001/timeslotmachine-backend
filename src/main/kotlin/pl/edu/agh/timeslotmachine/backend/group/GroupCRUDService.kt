package pl.edu.agh.timeslotmachine.backend.group

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.user.IndexNo
import kotlin.jvm.optionals.getOrNull

@Service
class GroupCRUDService(
    private val groupRepository: GroupRepository
) {
    fun getById(id: EID) =
        groupRepository.findById(id).getOrNull() ?: throw GroupNotFoundException(id)

    fun getById(groupId: EID, userId: EID) =
        groupRepository.findGroupByIdAndUsersId(groupId, userId) ?: throw GroupNotFoundException(groupId, userId)

    fun getAllGroups(): List<Group> =
        groupRepository.findAll()

    fun deleteById(id: EID) =
        groupRepository.deleteById(id)

    fun save(group: Group) =
        groupRepository.save(group)

    fun getGroupStatus(id: EID) =
        groupRepository.getGroupStatus(id)

    fun setGroupStatus(groupId: EID, status: GroupStatus) =
        groupRepository.setGroupStatus(groupId, status)

    @Transactional
    fun setGroupStatusInTransaction(groupId: EID, status: GroupStatus) =
        groupRepository.setGroupStatus(groupId, status)

    fun addUserByIndexNo(groupId: EID, indexNo: IndexNo) =
        groupRepository.addUserByIndexNo(groupId, indexNo)

    fun containsUser(groupId: EID, userId: EID) =
        groupRepository.containsUser(groupId, userId)

    fun getByExchangeId(exchangeId: EID) =
        groupRepository.getByExchangeId(exchangeId)

    fun getByConEventId(conEventId: EID) =
        groupRepository.getByConEventId(conEventId)

    fun getByIds(groupIds: Set<EID>) =
        groupRepository.getAllByIdIn(groupIds)

    fun unassignUserByIndexNo(groupId: EID, userIndexNo: IndexNo) =
        groupRepository.removeUserGroupByIndexNo(groupId, userIndexNo)

    fun containsUserByIndex(groupId: EID, userIndexNo: IndexNo) =
        groupRepository.containsUserByIndexNo(groupId, userIndexNo)

}