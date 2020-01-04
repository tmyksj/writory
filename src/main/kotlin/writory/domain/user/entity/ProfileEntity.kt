package writory.domain.user.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "profile")
data class ProfileEntity(
        @Id var id: String? = null,
        var username: String? = null,
        var description: String? = null,
        var link: String? = null,
        var externalGithub: String? = null,
        var externalTwitter: String? = null
) {

    var created: LocalDateTime? = null
        private set

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
