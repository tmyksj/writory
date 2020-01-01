package writory.service.authentication

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import writory.entity.UserEntity
import writory.exception.authentication.UserFoundException
import writory.repository.UserRepository
import java.util.*

@SpringBootTest
class AuthenticationServiceTests {

    @Autowired
    private lateinit var authenticationService: AuthenticationService

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
    fun signUpCreatesUser() {
        val email = "${UUID.randomUUID()}@example.com"
        authenticationService.signUp(email, "password")
        Assertions.assertThat(userRepository.findByEmail(email)).isNotNull
    }

    @Test
    fun signUpFailsIfUserFound() {
        Assertions.assertThatThrownBy {
            authenticationService.signUp(userEntity.email!!, "password")
        }.isInstanceOf(UserFoundException::class.java)
    }

}
