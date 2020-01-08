package writory.application.item.form

import javax.validation.constraints.NotBlank

data class ItemForm(

        @field:NotBlank
        var id: String? = null,

        var all: String? = null

)
