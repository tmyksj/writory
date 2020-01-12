package writory.domain.user.impl

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import writory.domain.user.UserDomain
import writory.domain.user.entity.UserEntity
import writory.domain.user.exception.PasswordMismatchException
import writory.domain.user.exception.UserFoundException
import writory.domain.user.exception.UserNotFoundException
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

    override fun modifyEmail(userId: String, email: String) {
        val entity: UserEntity = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        if (userRepository.findByEmail(email) == null) {
            entity.email = email
        } else {
            throw UserFoundException()
        }
    }

    override fun modifyPassword(userId: String, currentPasswordRaw: String, newPasswordRaw: String) {
        val entity: UserEntity = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        if (passwordEncoder.matches(currentPasswordRaw, entity.password)) {
            entity.password = passwordEncoder.encode(newPasswordRaw)
        } else {
            throw PasswordMismatchException()
        }
    }

    override fun reloadUser(userDetails: UserDetails) {
        if (userDetails !is UserPrincipal) {
            throw IllegalArgumentException()
        }

        userDetails.userEntity = userRepository.findByIdOrNull(userDetails.id)
                ?: throw UsernameNotFoundException("user principal not found")
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
