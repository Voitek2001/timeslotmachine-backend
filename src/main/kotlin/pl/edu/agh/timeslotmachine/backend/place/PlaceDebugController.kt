package pl.edu.agh.timeslotmachine.backend.place

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import pl.edu.agh.timeslotmachine.backend.annotation.DebugRestController

private val logger = KotlinLogging.logger {}

@DebugRestController
class PlaceDebugController(
    private val placeRepository: PlaceRepository
) {
    init {
        logger.debug { "init" }
    }

    @GetMapping("/places")
    fun getPlaces() = run {
        val places = placeRepository.findAll()
        logger.debug { places }
        places.asSequence()
    }
}