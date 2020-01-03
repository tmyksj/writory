package writory.domain.item.entity

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "item")
data class ItemEntity(
        var userId: String? = null,
        var title: String? = null
) {

    @Id
    var id: String? = null
        private set

    var created: LocalDateTime? = null
        private set

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
