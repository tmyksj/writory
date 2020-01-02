package writory.controller.item

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
import writory.entity.ItemEntity
import writory.entity.ItemSectionEntity
import writory.entity.UserEntity
import writory.repository.ItemRepository
import writory.repository.ItemSectionRepository
import writory.repository.UserRepository
import java.util.*
import javax.servlet.Filter

@SpringBootTest
class ItemControllerTests {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var springSecurityFilterChain: Filter

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var itemSectionRepository: ItemSectionRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var itemEntity: ItemEntity

    private lateinit var mockMvc: MockMvc

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
    fun buildsMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters<DefaultMockMvcBuilder>(springSecurityFilterChain)
                .build()
    }

    @Test
    fun indexRespondsOk() {
        mockMvc.perform(MockMvcRequestBuilders.get("/item/${itemEntity.id}"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

}
