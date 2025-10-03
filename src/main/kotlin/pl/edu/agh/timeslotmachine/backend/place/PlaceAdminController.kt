package pl.edu.agh.timeslotmachine.backend.place

import org.springframework.web.bind.annotation.*
import pl.edu.agh.timeslotmachine.backend.core.ADMIN_PATH
import pl.edu.agh.timeslotmachine.backend.annotation.SwaggerSessionSecurity
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.user.User

@RestController
@RequestMapping("$ADMIN_PATH/places")
@SwaggerSessionSecurity
class PlaceAdminController(
    private val placeService: PlaceService
) {

    @GetMapping("/")
    fun getAllPlaces() = placeService.getAllPlaces()

    @GetMapping("/{id}/")
    fun getPlace(@PathVariable id: EID) = placeService.getById(id)

    @PutMapping("/")
    fun updatePlace(@RequestBody root: PlaceRootInfo) = placeService.updatePlace(root.place)

    @PostMapping("/")
    fun createGroup(
        @RequestBody root: PlaceRootInfo,
        @RequestAttribute user: User
    ) = placeService.createPlace(root.place)

    @DeleteMapping("/{id}")
    fun deleteGroup(@PathVariable id: EID) = placeService.deleteById(id)

    data class PlaceRootInfo(val place: PlaceInfo)

}