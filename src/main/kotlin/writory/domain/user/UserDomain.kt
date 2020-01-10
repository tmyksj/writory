package writory.domain.user

import org.springframework.security.core.userdetails.UserDetailsService

interface UserDomain : UserDetailsService {

    fun modifyEmail(userId: String, email: String)

    fun modifyPassword(userId: String, currentPasswordRaw: String, newPasswordRaw: String)

    fun signUp(email: String, passwordRaw: String)

}
