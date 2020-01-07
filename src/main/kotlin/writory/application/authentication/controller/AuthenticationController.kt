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

@Controller
class AuthenticationController(
        private val userDomain: UserDomain
) {

    @RequestMapping(method = [RequestMethod.GET], path = ["/sign-in"])
    fun signIn(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            model: Model
    ): String {
        return if (userPrincipal == null) {
            "authentication/sign-in"
        } else {
            "redirect:/dashboard"
        }
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/sign-up"])
    fun signUp(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            form: SignUpForm,
            model: Model
    ): String {
        return if (userPrincipal == null) {
            "authentication/sign-up"
        } else {
            "redirect:/dashboard"
        }
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/sign-up"])
    fun signUpPost(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            @Validated form: SignUpForm,
            bindingResult: BindingResult,
            model: Model
    ): String {
        if (userPrincipal != null) {
            return "redirect:/dashboard"
        }

        if (bindingResult.hasErrors()) {
            return "authentication/sign-up"
        }

        return try {
            userDomain.signUp(form.email!!, form.password!!)
            "redirect:/sign-in"
        } catch (e: UserFoundException) {
            "authentication/sign-up"
        }
    }

}
