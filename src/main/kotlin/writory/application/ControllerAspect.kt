package writory.application

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import org.springframework.ui.Model
import writory.domain.user.principal.UserPrincipal

@Aspect
@Component
class ControllerAspect {

    @Before(value = "execution(* writory.application..*.controller..*.*(..))")
    fun addAttribute(joinPoint: JoinPoint) {
        val model: Model? = joinPoint.args.filterIsInstance<Model>().firstOrNull()

        model?.addAttribute("form",
                joinPoint.args.firstOrNull { it != null && (it::class.simpleName?.endsWith("Form") ?: false) })
        model?.addAttribute("user",
                joinPoint.args.filterIsInstance<UserPrincipal>().firstOrNull())
    }

}
