package writory.form.dashboard

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class ItemModifyForm {

    @NotBlank
    var id: String? = null

    @NotBlank
    var title: String? = null

    @Valid
    var sectionList: List<Section>? = null

    data class Section(

            var id: String? = null,

            @NotBlank
            var header: String? = null,

            @NotBlank
            var body: String? = null,

            @NotNull
            var star: Boolean? = false

    )

}
