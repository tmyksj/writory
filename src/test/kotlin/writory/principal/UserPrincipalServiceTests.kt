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
        userEntity = userRepository.save(UserEntity(
                email = "${UUID.randomUUID()}@example.com",
                password = "password"
        ))
    }

    @Test
    fun loadUserByUsernameReturnsUserPrincipal() {
        val userPrincipal: UserPrincipal? = userPrincipalService.loadUserByUsername(userEntity.email) as? UserPrincipal
        Assertions.assertThat(userPrincipal?.userEntity?.email).isEqualTo(userEntity.email)
    }

    @Test
    fun loadUserByUsernameThrowsUsernameNotFoundException() {
        Assertions.assertThatThrownBy {
            userPrincipalService.loadUserByUsername("${UUID.randomUUID()}@example.com")
        }.isInstanceOf(UsernameNotFoundException::class.java)
    }

}
