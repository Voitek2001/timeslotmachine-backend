package pl.edu.agh.timeslotmachine.backend.exchange.dto

import pl.edu.agh.timeslotmachine.backend.core.EID

data class ExchangeRequest(
    val userId1: EID,
    val userId2: EID,
    val swaps: Set<SwapDescription>
) {
    data class SwapDescription(
        val concreteEventId1: EID,
        val concreteEventId2: EID
    )
}