package writory.controller.home

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.principal.UserPrincipal

@Controller
class HomeController {

    @RequestMapping(method = [RequestMethod.GET], path = ["/"])
    fun index(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            model: Model
    ): String {
        model.addAttribute("user", userPrincipal?.userEntity)

        return if (userPrincipal == null) {
            "home/index"
        } else {
            "redirect:/dashboard"
        }
    }

}
