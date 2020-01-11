package writory.application.authentication.form

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class SignUpForm(

        @field:Email
        @field:NotBlank
        @field:Size(max = 255, min = 0)
        var email: String? = null,

        @field:NotBlank
        @field:Pattern(regexp = "^\\w+$")
        @field:Size(max = 255, min = 8)
        var password: String? = null

)
