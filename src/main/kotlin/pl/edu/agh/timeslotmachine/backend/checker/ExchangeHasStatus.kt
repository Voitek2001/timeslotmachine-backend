package pl.edu.agh.timeslotmachine.backend.checker

import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.exchange.ExchangeStatus

class ExchangeHasStatus(
    exchangeId: EID,
    status: ExchangeStatus,
) : ExchangeSatisfies(exchangeId, { it.status == status })