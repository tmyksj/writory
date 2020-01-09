package writory.application.item.controller

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
import writory.domain.item.entity.ItemEntity
import writory.domain.item.entity.ItemSectionEntity
import writory.domain.item.repository.ItemRepository
import writory.domain.item.repository.ItemSectionRepository
import writory.domain.user.entity.UserEntity
import writory.domain.user.repository.UserRepository
import java.util.*
import javax.servlet.Filter

@SpringBootTest
class ItemControllerTests {

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

    @BeforeEach
    fun buildsEntity() {
        val userEntity = userRepository.save(UserEntity(
                email = "${UUID.randomUUID()}@example.com",
                password = "password"
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

    @BeforeEach
    fun builds_MockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters<DefaultMockMvcBuilder>(springSecurityFilterChain)
                .build()
    }

    @Test
    fun item_responds_200() {
        mockMvc.perform(MockMvcRequestBuilders.get("/item/${itemEntity.id}"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun item_responds_404_when_item_does_not_exists() {
        mockMvc.perform(MockMvcRequestBuilders.get("/item/${UUID.randomUUID()}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

}
