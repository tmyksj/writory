package writory.application.dashboard.form

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class ConfigurationPasswordForm(

        @field:NotBlank
        @field:Pattern(regexp = "^\\w+$")
        @field:Size(max = 255, min = 8)
        var currentPassword: String? = null,

        @field:NotBlank
        @field:Pattern(regexp = "^\\w+$")
        @field:Size(max = 255, min = 8)
        var newPassword: String? = null

)
