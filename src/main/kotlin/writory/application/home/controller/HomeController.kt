package writory.application.home.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.domain.user.principal.UserPrincipal
import javax.servlet.http.HttpServletResponse

@Controller
class HomeController {

    @RequestMapping(method = [RequestMethod.GET], path = ["/"])
    fun getIndex(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        return if (userPrincipal == null) {
            "200:home/index"
        } else {
            "302:/dashboard"
        }
    }

}
