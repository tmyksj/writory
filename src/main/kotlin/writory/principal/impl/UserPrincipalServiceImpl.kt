package writory.principal.impl

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import writory.entity.UserEntity
import writory.principal.UserPrincipal
import writory.principal.UserPrincipalService
import writory.repository.UserRepository

@Service
@Transactional
class UserPrincipalServiceImpl(
        private val userRepository: UserRepository
) : UserPrincipalService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity: UserEntity = userRepository.findByEmail(username)
                ?: throw UsernameNotFoundException("username not found")
        return UserPrincipal(userEntity)
    }

}
