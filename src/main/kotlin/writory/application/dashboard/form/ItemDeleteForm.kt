package writory.application.dashboard.form

import javax.validation.constraints.NotBlank

data class ItemDeleteForm(

        @NotBlank
        var id: String? = null

)
