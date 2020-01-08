package writory.application.page.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.domain.user.principal.UserPrincipal

@Controller
class PageController {

    @RequestMapping(method = [RequestMethod.GET], path = ["/about"])
    fun about(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            model: Model
    ): String {
        return "page/about"
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/privacy-policy"])
    fun privacyPolicy(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            model: Model
    ): String {
        return "page/privacy-policy"
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/terms-of-service"])
    fun termsOfService(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            model: Model
    ): String {
        return "page/terms-of-service"
    }

}
