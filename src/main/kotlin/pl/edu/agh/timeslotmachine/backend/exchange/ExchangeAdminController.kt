package pl.edu.agh.timeslotmachine.backend.exchange

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.timeslotmachine.backend.core.ADMIN_PATH
import pl.edu.agh.timeslotmachine.backend.annotation.SwaggerSessionSecurity
import pl.edu.agh.timeslotmachine.backend.exchange.dto.ExchangeRequest
import pl.edu.agh.timeslotmachine.backend.user.User

@RestController
@RequestMapping("$ADMIN_PATH/exchange")
@SwaggerSessionSecurity
class ExchangeAdminController(
    private val exchangeService: ExchangeService
) {
    @PostMapping("/")
    fun doExchange(
        @RequestBody root: ExchangeRequestRoot,
        @RequestAttribute user: User
    ) = exchangeService.exchange(root.exchange)

    data class ExchangeRequestRoot(val exchange: ExchangeRequest)
}