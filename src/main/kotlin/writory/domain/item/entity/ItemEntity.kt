package writory.domain.item.entity

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "item")
data class ItemEntity(

        @field:NotBlank
        @field:Size(max = 36, min = 36)
        var userId: String? = null,

        @field:NotNull
        @field:Size(max = 65535, min = 0)
        var title: String? = null

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
