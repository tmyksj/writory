package writory.application.dashboard.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import writory.domain.item.entity.ItemEntity
import writory.domain.item.entity.ItemSectionEntity
import writory.domain.item.repository.ItemRepository
import writory.domain.item.repository.ItemSectionRepository
import writory.domain.user.entity.UserEntity
import writory.domain.user.principal.UserPrincipal
import writory.domain.user.repository.UserRepository
import java.util.*
import javax.servlet.Filter

@SpringBootTest
class DashboardControllerTests {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var springSecurityFilterChain: Filter

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var itemSectionRepository: ItemSectionRepository

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
                password = passwordEncoder.encode("password")
        ))

        itemEntity = itemRepository.save(ItemEntity(
                userId = userEntity.id,
                title = "title"
        ))

        itemSectionRepository.save(ItemSectionEntity(
                itemId = itemEntity.id,
                position = 0,
                header = "header",
                body = "body",
                star = true
        ))
    }

    @Test
    fun getConfiguration_responds_200() {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/configuration")
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun getIndex_responds_302() {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard")
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isFound)
    }

    @Test
    fun getItem_responds_200() {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/item/${itemEntity.id}")
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun getItem_responds_404_when_item_does_not_exists() {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/item/${UUID.randomUUID()}")
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun getItemList_responds_200() {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/item")
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun getItemModify_responds_200() {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/item/${itemEntity.id}/modify")
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun getItemModify_responds_400_when_item_does_not_exists() {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/item/${UUID.randomUUID()}/modify")
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun postConfigurationEmail_responds_302() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/configuration/email")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity)))
                .param("email", "${UUID.randomUUID()}@example.com"))
                .andExpect(MockMvcResultMatchers.status().isFound)
    }

    @Test
    fun postConfigurationEmail_responds_400_when_params_are_invalid() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/configuration/email")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity)))
                .param("email", "[invalid]email"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun postConfigurationPassword_responds_302() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/configuration/password")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity)))
                .param("currentPassword", "password")
                .param("newPassword", "newPassword"))
                .andExpect(MockMvcResultMatchers.status().isFound)
    }

    @Test
    fun postConfigurationPassword_responds_400_when_params_are_invalid() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/configuration/password")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity)))
                .param("currentPassword", "wrongPassword")
                .param("newPassword", "newPassword"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun postItem_responds_302() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/item")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isFound)
    }

    @Test
    fun postItemDelete_responds_302() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/item/${itemEntity.id}/delete")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isFound)
    }

    @Test
    fun postItemDelete_responds_400_when_item_does_not_exists() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/item/${UUID.randomUUID()}/delete")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun postItemModify_responds_302() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/item/${itemEntity.id}/modify")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity)))
                .param("title", "[modified]title")
                .param("sectionList[0].header", "header")
                .param("sectionList[0].body", "body")
                .param("sectionList[0].star", "true"))
                .andExpect(MockMvcResultMatchers.status().isFound)
    }

    @Test
    fun postItemModify_responds_400_when_item_does_not_exists() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/item/${UUID.randomUUID()}/modify")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity)))
                .param("title", "[modified]title")
                .param("sectionList[0].header", "header")
                .param("sectionList[0].body", "body")
                .param("sectionList[0].star", "true"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun postItemModify_responds_400_when_params_are_invalid() {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/item/${itemEntity.id}/modify")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(UserPrincipal(userEntity)))
                .param("sectionList[0].header", "header")
                .param("sectionList[0].body", "body")
                .param("sectionList[0].star", "true"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

}
