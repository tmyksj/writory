package writory.service.authentication.impl

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import writory.entity.UserEntity
import writory.exception.authentication.UserFoundException
import writory.repository.UserRepository
import writory.service.authentication.AuthenticationService

@Service
@Transactional
class AuthenticationServiceImpl(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder
) : AuthenticationService {

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
