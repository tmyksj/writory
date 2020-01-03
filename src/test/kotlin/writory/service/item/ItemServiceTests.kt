package writory.service.item

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import writory.entity.ItemEntity
import writory.entity.ItemSectionEntity
import writory.entity.UserEntity
import writory.exception.item.ItemNotFoundException
import writory.repository.ItemRepository
import writory.repository.ItemSectionRepository
import writory.repository.UserRepository
import java.util.*

@SpringBootTest
class ItemServiceTests {

    @Autowired
    private lateinit var itemService: ItemService

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var itemSectionRepository: ItemSectionRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var itemEntity: ItemEntity

    @BeforeEach
    fun savesUserEntity() {
        val userEntity: UserEntity = userRepository.save(UserEntity(
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

        itemSectionRepository.save(ItemSectionEntity(
                itemId = itemEntity.id,
                position = 1,
                header = "header",
                body = "body",
                star = true
        ))
    }

    @Test
    fun findItemReturnsItem() {
        val item: Pair<ItemEntity, List<ItemSectionEntity>> = itemService.findItem(itemEntity.id!!)
        Assertions.assertThat(item.second.size).isEqualTo(2)
        Assertions.assertThat(item.second.map { it.position }).isEqualTo(listOf(0, 1))
    }

    @Test
    fun findItemThrowsItemNotFoundException() {
        Assertions.assertThatThrownBy {
            itemService.findItem(UUID.randomUUID().toString())
        }.isInstanceOf(ItemNotFoundException::class.java)
    }

}
