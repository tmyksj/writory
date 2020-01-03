package writory.domain.user.impl

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import writory.domain.user.UserDomain
import writory.domain.user.entity.UserEntity
import writory.domain.user.exception.UserFoundException
import writory.domain.user.principal.UserPrincipal
import writory.domain.user.repository.UserRepository

@Component
@Transactional(propagation = Propagation.REQUIRED)
class UserDomainImpl(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder
) : UserDomain {

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity: UserEntity = userRepository.findByEmail(username)
                ?: throw UsernameNotFoundException("username not found")
        return UserPrincipal(userEntity)
    }

    override fun signUp(email: String, passwordRaw: String) {
        if (userRepository.findByEmail(email) == null) {
            userRepository.save(UserEntity(
                    email = email,
                    password = passwordEncoder.encode(passwordRaw)
            ))
        } else {
            throw UserFoundException()
        }
    }

}
