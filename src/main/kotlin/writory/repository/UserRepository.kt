package writory.repository

import org.springframework.data.jpa.repository.JpaRepository
import writory.entity.UserEntity

interface UserRepository : JpaRepository<UserEntity, String>
