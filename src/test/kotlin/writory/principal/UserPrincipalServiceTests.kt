package writory.principal

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UsernameNotFoundException
import writory.entity.UserEntity
import writory.repository.UserRepository
import java.util.*

@SpringBootTest
class UserPrincipalServiceTests {

    @Autowired
    private lateinit var userPrincipalService: UserPrincipalService

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var userEntity: UserEntity

    @BeforeEach
    fun savesUserEntity() {
        userEntity = UserEntity()
        userEntity.email = "${UUID.randomUUID()}@example.com"
        userEntity.password = "password"
        userRepository.save(userEntity)
    }

    @Test
    fun returnsUserPrincipal() {
        val userPrincipal: UserPrincipal? = userPrincipalService.loadUserByUsername(userEntity.email) as? UserPrincipal
        Assertions.assertThat(userPrincipal?.userEntity?.email).isEqualTo(userEntity.email)
    }

    @Test
    fun throwsUsernameNotFoundException() {
        Assertions.assertThatThrownBy {
            userPrincipalService.loadUserByUsername("${UUID.randomUUID()}@example.com")
        }.isInstanceOf(UsernameNotFoundException::class.java)
    }

}
