//package pl.edu.agh.timeslotmachine.backend.security.csrf
//
//import jakarta.servlet.http.HttpServletResponse
//import org.springframework.security.web.csrf.CsrfToken
//import org.springframework.web.bind.annotation.ControllerAdvice
//import org.springframework.web.bind.annotation.ModelAttribute
//
//@ControllerAdvice
//class CsrfControllerAdvice {
//
//    @ModelAttribute
//    fun getCsrfToken(response: HttpServletResponse, csrfToken: CsrfToken) {
//        response.setHeader(csrfToken.headerName, csrfToken.token)
//    }
//
//}