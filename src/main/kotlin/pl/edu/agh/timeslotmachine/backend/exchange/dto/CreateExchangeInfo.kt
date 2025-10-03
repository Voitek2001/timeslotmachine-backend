package pl.edu.agh.timeslotmachine.backend.exchange.dto

import pl.edu.agh.timeslotmachine.backend.core.EID

data class CreateExchangeInfo(
    val conEventOfferedId: EID?,
    val conEventRequestedIds: List<EID>,
    val isPrivate: Boolean
)