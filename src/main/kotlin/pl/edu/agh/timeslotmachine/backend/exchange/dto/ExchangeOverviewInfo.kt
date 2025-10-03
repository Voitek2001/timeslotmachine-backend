package pl.edu.agh.timeslotmachine.backend.exchange.dto

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.exchange.ExchangeStatus
import java.util.UUID

data class ExchangeOverviewInfo(
    val exchangeId: EID,
    val offeredConEvent: ExchangeInfo,
    val requestedConEvent: ExchangeInfo,
    val status: ExchangeStatus,
    val exchangeToken: UUID
)
