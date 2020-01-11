package writory.application

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.springframework.ui.Model
import writory.domain.user.principal.UserPrincipal
import javax.servlet.http.HttpServletResponse

@Aspect
@Component
class ControllerAspect {

    @Around(value = "execution(* writory.application..*.controller..*.*(..))")
    fun addAttribute(pjp: ProceedingJoinPoint): Any? {
        val model: Model? = pjp.args.filterIsInstance<Model>().firstOrNull()

        model?.addAttribute("form",
                pjp.args.firstOrNull { it != null && (it::class.simpleName?.endsWith("Form") ?: false) })
        model?.addAttribute("user",
                pjp.args.filterIsInstance<UserPrincipal>().firstOrNull()?.userEntity)

        val retVal: Any? = pjp.proceed()

        if (retVal is String && retVal.matches(Regex("^[2345]\\d{2}:.+"))) {
            val httpServletResponse: HttpServletResponse? =
                    pjp.args.filterIsInstance<HttpServletResponse>().firstOrNull()
            val statusCode: Int = retVal.substring(0, 3).toInt()
            val viewName: String = retVal.substring(4)

            httpServletResponse?.status = statusCode
            model?.addAttribute("httpStatusCode${statusCode}", true)

            return if (statusCode in 300..399) {
                if (httpServletResponse == null) {
                    "redirect:${viewName}"
                } else {
                    httpServletResponse.setHeader("Location", viewName)
                    null
                }
            } else {
                viewName
            }
        }

        return retVal
    }

}
