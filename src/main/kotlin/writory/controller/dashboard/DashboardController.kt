package writory.controller.dashboard

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.principal.UserPrincipal

@Controller
class DashboardController {

    @RequestMapping(method = [RequestMethod.GET], path = ["/dashboard"])
    fun index(
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            model: Model
    ): String {
        return "dashboard/index"
    }

}
