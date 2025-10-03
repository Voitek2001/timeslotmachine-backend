package pl.edu.agh.timeslotmachine.backend.annotation

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

@ConditionalOnProperty(prefix = "timeslotmachine.backend", name = ["debug"])
annotation class DebugOnly
