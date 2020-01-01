package writory.form.authentication

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class SignUpForm {

    @Email
    @NotBlank
    var email: String? = null

    @NotBlank
    var password: String? = null

}
