package pl.edu.agh.timeslotmachine.backend.exchange.dto

data class ExchangeData(
    val exchangesInfo: List<ExchangeInfo>,
    val existingExchangesInfo: List<ExistingExchangeInfo>,
    val currEventExchangeInfo: ExchangeInfo
)