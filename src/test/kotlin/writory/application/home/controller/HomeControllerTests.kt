package writory.application.home.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import writory.domain.user.entity.UserEntity
import writory.domain.user.principal.UserPrincipal
import writory.domain.user.repository.UserRepository
import java.util.*
import javax.servlet.Filter

@SpringBootTest
class HomeControllerTests {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var springSecurityFilterChain: Filter

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var userEntity: UserEntity

    @BeforeEach
    fun builds_MockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters<DefaultMockMvcBuilder>(springSecurityFilterChain)
                .build()
    }

    @BeforeEach
    fun saves_entities() {
        userEntity = userRepository.save(UserEntity(
                email = "${UUID.randomUUID()}@example.com",
                password = "password"
        ))
    }

    @Test
    fun getIndex_responds_200() {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun getIndex_responds_302_when_user_signed_in() {
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isFound)
    }

}
