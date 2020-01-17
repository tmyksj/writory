package writory.application.dashboard.form

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class ItemForm(

        @field:NotBlank
        @field:Size(max = 255, min = 0)
        var id: String? = null,

        @field:Size(max = 8, min = 0)
        var all: String? = null

)
