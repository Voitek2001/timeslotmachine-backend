package pl.edu.agh.timeslotmachine.backend.annotation

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@DebugOnly
@RequestMapping("/debug")
@RestController
annotation class DebugRestController()
