package writory.application.page.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.domain.user.principal.UserPrincipal
import javax.servlet.http.HttpServletResponse

@Controller
class PageController {

    @RequestMapping(method = [RequestMethod.GET], path = ["/about"])
    fun getAbout(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        return "200:page/about"
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/privacy-policy"])
    fun getPrivacyPolicy(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        return "200:page/privacy-policy"
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/terms-of-service"])
    fun getTermsOfService(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        return "200:page/terms-of-service"
    }

}
