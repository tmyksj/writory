package writory.controller.authentication

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.exception.authentication.UserFoundException
import writory.form.authentication.SignUpForm
import writory.principal.UserPrincipal
import writory.service.authentication.AuthenticationService

@Controller
class AuthenticationController(
        private val authenticationService: AuthenticationService
) {

    @RequestMapping(method = [RequestMethod.GET], path = ["/sign-in"])
    fun signIn(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            model: Model
    ): String {
        model.addAttribute("user", userPrincipal?.userEntity)

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
        model.addAttribute("user", userPrincipal?.userEntity)
        model.addAttribute("form", form)

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
        model.addAttribute("user", userPrincipal?.userEntity)
        model.addAttribute("form", form)

        if (userPrincipal != null) {
            return "redirect:/dashboard"
        }

        if (bindingResult.hasErrors()) {
            return "authentication/sign-up"
        }

        return try {
            authenticationService.signUp(form.email!!, form.password!!)
            "redirect:/sign-in"
        } catch (e: UserFoundException) {
            "authentication/sign-up"
        }
    }

}
