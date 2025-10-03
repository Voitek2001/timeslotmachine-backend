package pl.edu.agh.timeslotmachine.backend.checker

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.exchange.Exchange
import pl.edu.agh.timeslotmachine.backend.exchange.ExchangeService

open class ExchangeSatisfies(
    private val exchangeId: EID,
    private val pred: (Exchange) -> Boolean
) : Check {
    override fun invoke(context: CheckerContext) = context.storage.storedOrCreate<Exchange>(exchangeId) {
        context.bean<ExchangeService>().getById(exchangeId)
    }.let { pred(it) }
}