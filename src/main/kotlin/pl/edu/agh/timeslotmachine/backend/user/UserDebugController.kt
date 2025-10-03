package pl.edu.agh.timeslotmachine.backend.user

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import pl.edu.agh.timeslotmachine.backend.annotation.DebugRestController

private val logger = KotlinLogging.logger {}

@DebugRestController
class UserDebugController(
    private val userRepository: UserRepository
) {
    init {
        logger.debug { "init" }
    }

    @GetMapping("/users")
    fun getUsers() = run {
        val users = userRepository.findAll()
        logger.debug { users }
        users.asSequence()
    }
}