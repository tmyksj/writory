package writory.domain.item.entity

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "item_section")
data class ItemSectionEntity(
        var itemId: String? = null,
        var position: Int? = null,
        var header: String? = null,
        var body: String? = null,
        var star: Boolean? = null
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
