package writory.application.authentication.form

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignUpForm(

        @Email
        @NotBlank
        var email: String? = null,

        @NotBlank
        var password: String? = null

)
