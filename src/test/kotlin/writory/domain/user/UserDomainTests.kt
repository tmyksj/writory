package writory.domain.user

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UsernameNotFoundException
import writory.domain.user.entity.UserEntity
import writory.domain.user.exception.UserFoundException
import writory.domain.user.principal.UserPrincipal
import writory.domain.user.repository.UserRepository
import java.util.*

@SpringBootTest
class UserDomainTests {

    @Autowired
    private lateinit var userDomain: UserDomain

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var userEntity: UserEntity

    @BeforeEach
    fun savesUserEntity() {
        userEntity = userRepository.save(UserEntity(
                email = "${UUID.randomUUID()}@example.com",
                password = "password"
        ))
    }

    @Test
    fun loadUserByUsernameReturnsUserPrincipal() {
        val userPrincipal: UserPrincipal? = userDomain.loadUserByUsername(userEntity.email) as? UserPrincipal
        Assertions.assertThat(userPrincipal?.userEntity?.email).isEqualTo(userEntity.email)
    }

    @Test
    fun loadUserByUsernameThrowsUsernameNotFoundException() {
        Assertions.assertThatThrownBy {
            userDomain.loadUserByUsername("${UUID.randomUUID()}@example.com")
        }.isInstanceOf(UsernameNotFoundException::class.java)
    }

    @Test
    fun signUpCreatesUser() {
        val email = "${UUID.randomUUID()}@example.com"
        userDomain.signUp(email, "password")
        Assertions.assertThat(userRepository.findByEmail(email)).isNotNull
    }

    @Test
    fun signUpFailsIfUserFound() {
        Assertions.assertThatThrownBy {
            userDomain.signUp(userEntity.email!!, "password")
        }.isInstanceOf(UserFoundException::class.java)
    }

}
