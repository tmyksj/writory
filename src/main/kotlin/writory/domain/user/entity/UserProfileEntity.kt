package writory.domain.user.entity

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "user_profile")
data class UserProfileEntity(

        @field:Id
        @field:NotBlank
        @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
        var userId: String? = null,

        @field:Size(max = 255, min = 0)
        var username: String? = null,

        @field:Size(max = 255, min = 0)
        var description: String? = null,

        @field:Size(max = 255, min = 0)
        var link: String? = null,

        @field:Size(max = 255, min = 0)
        var externalGithub: String? = null,

        @field:Size(max = 255, min = 0)
        var externalTwitter: String? = null

) {

    @NotNull
    var created: LocalDateTime? = null
        private set

    @NotNull
    var modified: LocalDateTime? = null
        private set

    @PrePersist
    fun prePersist() {
        val now: LocalDateTime = LocalDateTime.now()
        created = now
        modified = now
    }

    @PreUpdate
    fun preUpdate() {
        modified = LocalDateTime.now()
    }

}
