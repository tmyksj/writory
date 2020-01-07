package writory.domain.user.entity

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "user")
data class UserEntity(

        @field:NotBlank
        @field:Size(max = 255, min = 0)
        var email: String? = null,

        @field:NotBlank
        @field:Size(max = 255, min = 0)
        var password: String? = null

) {

    @Id
    @NotBlank
    @Size(max = 36, min = 36)
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
