package writory.domain.legal.entity

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "privacy_policy")
data class PrivacyPolicyEntity(

        @field:NotNull
        @field:Size(max = 21844, min = 0)
        var body: String? = null

) {

    @Id
    @NotBlank
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
    var id: String? = null
        private set

    @NotNull
    var created: LocalDateTime? = null
        private set

    @NotNull
    var modified: LocalDateTime? = null
        private set

    @PrePersist
    fun prePersist() {
        id = UUID.randomUUID().toString()

        val now: LocalDateTime = LocalDateTime.now()
        created = now
        modified = now
    }

    @PreUpdate
    fun preUpdate() {
        modified = LocalDateTime.now()
    }

}
