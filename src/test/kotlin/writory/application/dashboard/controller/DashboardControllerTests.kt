package writory.application.dashboard.controller

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
import writory.domain.item.entity.ItemEntity
import writory.domain.item.repository.ItemRepository
import writory.domain.user.entity.UserEntity
import writory.domain.user.principal.UserPrincipal
import writory.domain.user.repository.UserRepository
import java.util.*
import javax.servlet.Filter

@SpringBootTest
class DashboardControllerTests {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var springSecurityFilterChain: Filter

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var itemEntity: ItemEntity

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

        itemEntity = itemRepository.save(ItemEntity(
                userId = userEntity.id,
                title = "title"
        ))
    }

    @Test
    fun index_responds_3xx() {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard")
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
    }

    @Test
    fun item_responds_200() {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/item")
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun itemCreatePost_responds_3xx() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/item/create")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
    }

    @Test
    fun itemDeletePost_responds_3xx() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/item/delete/${itemEntity.id}")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
    }

    @Test
    fun itemDeletePost_responds_3xx_when_item_does_not_exists() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/item/delete/${UUID.randomUUID()}")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
    }

    @Test
    fun itemModify_responds_200() {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/item/modify/${itemEntity.id}")
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun itemModify_responds_400_when_item_does_not_exists() {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/item/modify/${UUID.randomUUID()}")
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun itemModifyPost_responds_3xx() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/item/modify/${itemEntity.id}")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity)))
                .param("title", "[modified]title")
                .param("sectionList[0].header", "header")
                .param("sectionList[0].body", "body")
                .param("sectionList[0].star", "true"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
    }

    @Test
    fun itemModifyPost_responds_400_when_item_does_not_exists() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/item/modify/${UUID.randomUUID()}")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity)))
                .param("title", "[modified]title")
                .param("sectionList[0].header", "header")
                .param("sectionList[0].body", "body")
                .param("sectionList[0].star", "true"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
    }

    @Test
    fun itemModifyPost_responds_400_when_params_are_invalid() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/item/modify/${itemEntity.id}")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity)))
                .param("sectionList[0].header", "header")
                .param("sectionList[0].body", "body")
                .param("sectionList[0].star", "true"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

}
