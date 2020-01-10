package writory.application.dashboard.form

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class ConfigurationEmailForm(

        @field:Email
        @field:NotBlank
        @field:Size(max = 255, min = 0)
        var email: String? = null

)
