package writory.domain.user

import org.springframework.security.core.userdetails.UserDetailsService

interface UserDomain : UserDetailsService {

    fun signUp(email: String, passwordRaw: String)

}
