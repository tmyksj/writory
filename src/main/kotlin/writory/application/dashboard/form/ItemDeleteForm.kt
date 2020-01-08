package writory.application.dashboard.form

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class ItemDeleteForm(

        @field:NotBlank
        @field:Size(max = 255, min = 0)
        var id: String? = null

)
