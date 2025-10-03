package pl.edu.agh.timeslotmachine.backend.exchange.dto

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.exchange.ExchangeStatus
import java.util.*

data class ExistingExchangeInfo(
    val id: EID,
    val status: ExchangeStatus,
    val offered: ExchangeInfo,
    val exchangeToken: UUID
)
