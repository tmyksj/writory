package writory.application.authentication.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.application.authentication.form.SignUpForm
import writory.domain.user.UserDomain
import writory.domain.user.exception.UserFoundException
import writory.domain.user.principal.UserPrincipal
import javax.servlet.http.HttpServletResponse

@Controller
class AuthenticationController(
        private val userDomain: UserDomain
) {

    @RequestMapping(method = [RequestMethod.GET], path = ["/sign-in"])
    fun getSignIn(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        return if (userPrincipal == null) {
            "200:authentication/sign-in"
        } else {
            "302:/dashboard"
        }
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/sign-up"])
    fun getSignUp(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            form: SignUpForm,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        return if (userPrincipal == null) {
            "200:authentication/sign-up"
        } else {
            "302:/dashboard"
        }
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/sign-up"])
    fun postSignUp(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            @Validated form: SignUpForm,
            bindingResult: BindingResult,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        if (userPrincipal != null) {
            return "302:/dashboard"
        }

        if (bindingResult.hasErrors()) {
            return "400:authentication/sign-up"
        }

        return try {
            userDomain.signUp(form.email!!, form.password!!)
            "302:/sign-in"
        } catch (e: UserFoundException) {
            "400:authentication/sign-up"
        }
    }

}
