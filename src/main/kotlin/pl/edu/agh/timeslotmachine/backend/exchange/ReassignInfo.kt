package pl.edu.agh.timeslotmachine.backend.exchange

import pl.edu.agh.timeslotmachine.backend.event.ConcreteEventOverview

data class ReassignInfo(
    val userConEvents: List<ConcreteEventOverview>,
    val allGroupConEvents: List<ConcreteEventOverview>
)
