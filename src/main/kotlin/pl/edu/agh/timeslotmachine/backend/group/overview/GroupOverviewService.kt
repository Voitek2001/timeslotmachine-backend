package pl.edu.agh.timeslotmachine.backend.group.overview

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.timeslotmachine.backend.checker.*
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEventService
import pl.edu.agh.timeslotmachine.backend.event.EventService
import pl.edu.agh.timeslotmachine.backend.group.Group
import pl.edu.agh.timeslotmachine.backend.group.GroupCRUDService
import pl.edu.agh.timeslotmachine.backend.group.GroupStatus
import pl.edu.agh.timeslotmachine.backend.preference.PreferenceService
import pl.edu.agh.timeslotmachine.backend.user.User

@Service
class GroupOverviewService(
    private val groupCRUDService: GroupCRUDService,
    private val preferenceService: PreferenceService,
    private val eventService: EventService,
    private val concreteEventService: ConcreteEventService
) : Checker() {
    // TODO: doc, write about limitations
    @Transactional
    fun getOverview(groupId: EID, userId: EID) = require(
        UserInGroup(userId, groupId),
        GroupHasStatus(groupId, GroupStatus.Open)
    ).then {
        val allConcretes = eventService.findByGroupIdAndUserId(groupId, userId).flatMap { it.concreteEvents }

        val preferences = preferenceService.findByGroupIdAndUserId(groupId, userId).map {
            PreferenceSummary(
                concreteEventId = it.id.concreteEventId,
                points = it.points,
                isImpossible = it.isImpossible,
                impossibilityJustification = it.impossibilityJustification
            )
        }

        GroupOverview(
            group = toGroupSummary(stored<Group>(groupId), uniqueEventIds(allConcretes)),
            week = concreteEventService.getWeeklyOverview(allConcretes),
            preferences = preferences
        )
    }

    fun getAssignedOverview(groupId: EID, userId: EID) = require(
        UserInGroup(userId, groupId),
        GroupSatisfies(groupId) { it.status == GroupStatus.Available || it.status == GroupStatus.Exchange }
    ).then {
        preferenceService.getAssigned(groupId, userId).map {
            it.concreteEvent!!
        }.let {
            GroupAssignedOverview(
                group = toGroupSummary(stored<Group>(groupId), uniqueEventIds(it)),
                week = concreteEventService.getWeeklyOverview(it)
            )
        }
    }

    fun getUserGroups(user: User) =
        groupCRUDService.getByIds(user.groups.map { it.id!! }.toSet())

    private fun uniqueEventIds(concretes: List<ConcreteEvent>) =
        concretes.map { it.event.id!! }.toSet()

    private fun toGroupSummary(group: Group, eventIds: Set<EID>) = GroupSummary(
        id = group.id!!,
        name = group.name,
        description = group.description,
        maxPointsPerConcrete = group.maxPointsPerConcrete,
        events = group.events.filter { it.id in eventIds },
        status = group.status
    )
}