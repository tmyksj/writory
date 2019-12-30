package writory.controller.authentication

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.principal.UserPrincipal

@Controller
class AuthenticationController {

    @RequestMapping(method = [RequestMethod.GET], path = ["/sign-in"])
    fun signIn(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            model: Model
    ): String {
        return "authentication/sign-in"
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/sign-up"])
    fun signUp(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            model: Model
    ): String {
        return "authentication/sign-up"
    }

}
