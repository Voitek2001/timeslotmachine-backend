package pl.edu.agh.timeslotmachine.backend.group

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import pl.edu.agh.timeslotmachine.backend.annotation.DebugRestController

private val logger = KotlinLogging.logger {}

@DebugRestController
class GroupDebugController(
    private val groupRepository: GroupRepository
) {
    init {
        logger.debug { "init" }
    }

    @GetMapping("/groups")
    fun getGroups() = run {
        val groups = groupRepository.findAll()
        logger.debug { groups.map { it.events } }
        groups.asSequence()
    }
}