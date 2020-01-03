package writory.service.dashboard

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import writory.entity.ItemEntity
import writory.entity.ItemSectionEntity
import writory.entity.UserEntity
import writory.exception.dashboard.ItemModifyException
import writory.exception.dashboard.ItemNotFoundException
import writory.exception.dashboard.ItemPermissionException
import writory.repository.ItemRepository
import writory.repository.ItemSectionRepository
import writory.repository.UserRepository
import java.util.*

@SpringBootTest
class DashboardServiceTests {

    @Autowired
    private lateinit var dashboardService: DashboardService

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var itemSectionRepository: ItemSectionRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var itemEntity: ItemEntity

    private lateinit var itemSectionEntity0: ItemSectionEntity

    private lateinit var itemSectionEntity1: ItemSectionEntity

    private lateinit var userEntity: UserEntity

    @BeforeEach
    fun savesEntity() {
        userEntity = userRepository.save(UserEntity(
                email = "${UUID.randomUUID()}@example.com",
                password = "password"
        ))

        itemEntity = itemRepository.save(ItemEntity(
                userId = userEntity.id,
                title = "title"
        ))

        itemSectionEntity0 = itemSectionRepository.save(ItemSectionEntity(
                itemId = itemEntity.id,
                position = 0,
                header = "header",
                body = "body",
                star = true
        ))

        itemSectionEntity1 = itemSectionRepository.save(ItemSectionEntity(
                itemId = itemEntity.id,
                position = 1,
                header = "header",
                body = "body",
                star = true
        ))
    }

    @Test
    fun createItemCreatesItem() {
        val itemEntity: ItemEntity = dashboardService.createItem(userEntity.id!!)
        Assertions.assertThat(itemRepository.findById(itemEntity.id!!)).isNotNull
    }

    @Test
    fun findItemReturnsItem() {
        val item: Pair<ItemEntity, List<ItemSectionEntity>> =
                dashboardService.findItem(userEntity.id!!, itemEntity.id!!)
        Assertions.assertThat(item.second.size).isEqualTo(2)
        Assertions.assertThat(item.second.map { it.position }).isEqualTo(listOf(0, 1))
    }

    @Test
    fun findItemThrowsItemNotFoundException() {
        Assertions.assertThatThrownBy {
            dashboardService.findItem(userEntity.id!!, UUID.randomUUID().toString())
        }.isInstanceOf(ItemNotFoundException::class.java)
    }

    @Test
    fun findItemThrowsItemPermissionException() {
        val otherUserEntity: UserEntity = userRepository.save(UserEntity(
                email = "${UUID.randomUUID()}@example.com",
                password = "password"
        ))

        Assertions.assertThatThrownBy {
            dashboardService.findItem(otherUserEntity.id!!, itemEntity.id!!)
        }.isInstanceOf(ItemPermissionException::class.java)
    }

    @Test
    fun modifyItemModifiesItem0() {
        dashboardService.modifyItem(userEntity.id!!,
                Pair(itemEntity.id!!, itemEntity.copy(title = "title(modified)")),
                listOf(Pair(itemSectionEntity0.id, itemSectionEntity0.copy(header = "header(modified)")),
                        Pair(null, itemSectionEntity1.copy(header = "header(modified)"))))
        Assertions.assertThat(itemRepository.findById(itemEntity.id!!).get().title)
                .isEqualTo("title(modified)")
        Assertions.assertThat(itemSectionRepository.findById(itemSectionEntity0.id!!).get().header)
                .isEqualTo("header(modified)")
        Assertions.assertThat(itemSectionRepository.findById(itemSectionEntity1.id!!).isEmpty).isTrue()
    }

    @Test
    fun modifyItemModifiesItem1() {
        val itemSectionEntity2: ItemSectionEntity = itemSectionRepository.save(ItemSectionEntity(
                itemId = itemEntity.id,
                position = 2,
                header = "header",
                body = "body",
                star = true
        ))

        dashboardService.modifyItem(userEntity.id!!,
                Pair(itemEntity.id!!, itemEntity.copy()),
                listOf(Pair(itemSectionEntity0.id, itemSectionEntity0.copy(header = "header(modified)")),
                        Pair(itemSectionEntity2.id, itemSectionEntity2.copy(header = "header(modified)", position = 1)),
                        Pair(null, itemSectionEntity1.copy(header = "header(modified)", position = 2))))
    }

    @Test
    fun modifyItemThrowsItemModifyException() {
        val otherUserEntity: UserEntity = userRepository.save(UserEntity(
                email = "${UUID.randomUUID()}@example.com",
                password = "password"
        ))

        val otherItemEntity: ItemEntity = itemRepository.save(ItemEntity(
                userId = otherUserEntity.id,
                title = "title"
        ))

        Assertions.assertThatThrownBy {
            dashboardService.modifyItem(userEntity.id!!,
                    Pair(UUID.randomUUID().toString(), itemEntity.copy()),
                    listOf())
        }.isInstanceOf(ItemModifyException::class.java)

        Assertions.assertThatThrownBy {
            dashboardService.modifyItem(userEntity.id!!,
                    Pair(itemEntity.id!!, itemEntity.copy()),
                    listOf(Pair(UUID.randomUUID().toString(), itemSectionEntity0.copy())))
        }.isInstanceOf(ItemModifyException::class.java)

        Assertions.assertThatThrownBy {
            dashboardService.modifyItem(otherUserEntity.id!!,
                    Pair(itemEntity.id!!, itemEntity.copy()),
                    listOf(Pair(itemSectionEntity0.id, itemSectionEntity0.copy())))
        }.isInstanceOf(ItemModifyException::class.java)

        Assertions.assertThatThrownBy {
            dashboardService.modifyItem(otherUserEntity.id!!,
                    Pair(otherItemEntity.id!!, otherItemEntity),
                    listOf(Pair(itemSectionEntity0.id, itemSectionEntity0.copy())))
        }.isInstanceOf(ItemModifyException::class.java)
    }

}
