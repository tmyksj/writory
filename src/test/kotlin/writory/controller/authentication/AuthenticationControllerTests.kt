package writory.controller.authentication

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.servlet.Filter

@SpringBootTest
class AuthenticationControllerTests {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var springSecurityFilterChain: Filter

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun buildsMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters<DefaultMockMvcBuilder>(springSecurityFilterChain)
                .build()
    }

    @Test
    fun signInRespondsOk() {
        mockMvc.perform(MockMvcRequestBuilders.get("/sign-in"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun signUpRespondsOk() {
        mockMvc.perform(MockMvcRequestBuilders.get("/sign-up"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

}
