package pl.edu.agh.timeslotmachine.backend.exchange

import org.springframework.web.bind.annotation.*
import pl.edu.agh.timeslotmachine.backend.annotation.SwaggerSessionSecurity
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.exchange.dto.CreateExchangeInfo
import pl.edu.agh.timeslotmachine.backend.exchange.dto.ExchangeData
import pl.edu.agh.timeslotmachine.backend.exchange.dto.ExchangeOverviewInfo
import pl.edu.agh.timeslotmachine.backend.exchange.dto.PrivateExchangeInfo
import pl.edu.agh.timeslotmachine.backend.user.User


@RestController
@RequestMapping("/exchanges")
@SwaggerSessionSecurity
class ExchangeController(
    private val exchangeService: ExchangeService
) {

    @GetMapping("/{conEventId}/")
    fun getExchangeData(
        @PathVariable conEventId: EID,
        @RequestAttribute user: User
    ) = ExchangeDataRoot(exchangeService.getExchangeData(conEventId, user)!!)

    @GetMapping("/overview/{groupId}/")
    fun getExchangesOverview(
        @PathVariable groupId: EID,
        @RequestAttribute user: User
    ) = ExchangeOverviewRoot(exchangeService.getOverview(user, groupId)!!)

    @PostMapping("/")
    fun createExchange(
        @RequestBody createExchange: CreateExchangeRoot,
        @RequestAttribute user: User
    ) = exchangeService.createExchange(createExchange.createExchange, user)

    @PutMapping("/{exchangeId}/")
    fun makeExchange(
        @PathVariable exchangeId: EID,
        @RequestAttribute user: User
    ) = exchangeService.makeExchange(exchangeId, user)

    @PutMapping("/cancel/{exchangeId}/")
    fun cancelExchange(
        @PathVariable exchangeId: EID,
        @RequestAttribute user: User
    ) = exchangeService.cancelExchange(exchangeId)

    @PutMapping("/private/")
    fun makePrivateExchange(
        @RequestBody privateExchangeInfoRoot: PrivateExchangeInfoRoot,
        @RequestAttribute user: User
    ) = exchangeService.makePrivateExchange(privateExchangeInfoRoot.privateExchangeInfo, user)

    data class CreateExchangeRoot(val createExchange: CreateExchangeInfo)

    data class ExchangeOverviewRoot(val exchangeOverviewInfo: List<ExchangeOverviewInfo>)

    data class ExchangeDataRoot(val exchangeData: ExchangeData)

    data class PrivateExchangeInfoRoot(val privateExchangeInfo: PrivateExchangeInfo)
}