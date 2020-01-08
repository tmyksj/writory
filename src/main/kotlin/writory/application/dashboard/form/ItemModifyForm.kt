package writory.application.dashboard.form

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ItemModifyForm(

        @field:NotBlank
        @field:Size(max = 255, min = 0)
        var id: String? = null,

        @field:NotNull
        @field:Size(max = 255, min = 0)
        var title: String? = null,

        @field:Valid
        var sectionList: List<Section>? = null

) {

    data class Section(

            @field:Size(max = 255, min = 0)
            var id: String? = null,

            @field:NotNull
            @field:Size(max = 255, min = 0)
            var header: String? = null,

            @field:NotNull
            @field:Size(max = 21844, min = 0)
            var body: String? = null,

            @field:NotNull
            var star: Boolean? = false

    )

}
