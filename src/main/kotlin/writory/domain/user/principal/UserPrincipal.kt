package writory.domain.user.principal

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import writory.domain.user.entity.UserEntity

class UserPrincipal(
        val userEntity: UserEntity
) : User(userEntity.email, userEntity.password, mutableListOf(SimpleGrantedAuthority("ROLE_USER")))
