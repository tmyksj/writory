package writory.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import writory.domain.user.entity.UserEntity

@Repository
interface UserRepository : JpaRepository<UserEntity, String> {

    fun findByEmail(email: String): UserEntity?

}
