package writory.service.dashboard

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.password.PasswordEncoder
import writory.domain.user.UserDomain
import writory.domain.user.entity.UserEntity
import writory.domain.user.principal.UserPrincipal
import java.util.*

@SpringBootTest
class DashboardServiceTests {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var dashboardService: DashboardService

    @MockBean
    private lateinit var userDomain: UserDomain

    @Mock
    private lateinit var userEntity: UserEntity

    @BeforeEach
    fun builds_mocks() {
        Mockito.`when`(userEntity.id).thenReturn(UUID.randomUUID().toString())
        Mockito.`when`(userEntity.email).thenReturn("${UUID.randomUUID()}@example.com")
        Mockito.`when`(userEntity.password).thenReturn(passwordEncoder.encode("password"))
    }

    @Test
    fun modifyEmail_calls_userDomain() {
        val user = UserPrincipal(userEntity)
        val email = "${UUID.randomUUID()}@example.com"

        dashboardService.modifyEmail(user, email)
        Mockito.verify(userDomain, Mockito.times(1)).modifyEmail(user.userEntity.id!!, email)
        Mockito.verify(userDomain, Mockito.times(1)).reloadUser(user)
    }

    @Test
    fun modifyPassword_calls_userDomain() {
        val user = UserPrincipal(userEntity)
        val current = "password"
        val new = "password"

        dashboardService.modifyPassword(user, current, new)
        Mockito.verify(userDomain, Mockito.times(1)).modifyPassword(user.userEntity.id!!, current, new)
        Mockito.verify(userDomain, Mockito.times(1)).reloadUser(user)
    }

}
