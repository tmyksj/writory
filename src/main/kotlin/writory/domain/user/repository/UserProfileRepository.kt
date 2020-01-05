package writory.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import writory.domain.user.entity.UserProfileEntity

@Repository
interface UserProfileRepository : JpaRepository<UserProfileEntity, String>
