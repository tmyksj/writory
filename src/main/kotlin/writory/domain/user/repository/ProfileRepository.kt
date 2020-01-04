package writory.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import writory.domain.user.entity.ProfileEntity

@Repository
interface ProfileRepository : JpaRepository<ProfileEntity, String>
