package pl.edu.agh.timeslotmachine.backend.place

import org.springframework.stereotype.Service
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.util.algorithm.validateNotNull
import kotlin.jvm.optionals.getOrNull

@Service
class PlaceService(
    private val placeRepository: PlaceRepository
) {
    fun getById(id: EID) =
        placeRepository.findById(id).getOrNull() ?: throw PlaceNotFoundException(id)

    fun getByNameAndRoom(name: String, room: String) =
        placeRepository.findByNameAndRoom(name, room)

    fun getAllPlaces(): List<Place> =
        placeRepository.findAll()

    fun getByNameAndRoomOrCreate(placeInfo: PlaceInfo) = placeInfo.apply {
        validateNotNull(::name, ::room)
    }.let {
        getByNameAndRoom(it.name!!, it.room!!) ?: createPlace(it)
    }

    fun createPlace(placeInfo: PlaceInfo) = placeInfo.apply {
        validateNotNull(::name, ::description, ::room)
    }.run {
        Place(
            name = name!!,
            description = description!!,
            localization = localization,
            room = room!!,
        ).also { placeRepository.save(it) }
    }

    fun deleteById(id: EID) =
        placeRepository.deleteById(id)

    fun updatePlace(placeInfo: PlaceInfo) = placeInfo.apply {
        validateNotNull(
            ::name,
            ::description,
            ::room,
            ::localization,
            ::id)
    }.let {
        getById(placeInfo.id!!).run {
            name = placeInfo.name!!
            description = placeInfo.description!!
            room = placeInfo.room!!
            localization = placeInfo.localization!!
            placeRepository.save(this)
        }
    }
}