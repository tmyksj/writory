package writory.domain.item

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import writory.domain.item.entity.ItemEntity
import writory.domain.item.entity.ItemSectionEntity
import writory.domain.item.exception.ItemModifyException
import writory.domain.item.exception.ItemNotFoundException
import writory.domain.item.repository.ItemRepository
import writory.domain.item.repository.ItemSectionRepository
import writory.domain.user.entity.UserEntity
import writory.domain.user.repository.UserRepository
import java.util.*

@SpringBootTest
class ItemDomainTests {

    @Autowired
    private lateinit var itemDomain: ItemDomain

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var itemSectionRepository: ItemSectionRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var itemEntity: ItemEntity

    private lateinit var itemSectionEntity0: ItemSectionEntity

    private lateinit var itemSectionEntity1: ItemSectionEntity

    private lateinit var itemSectionEntity2: ItemSectionEntity

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

        itemSectionEntity2 = itemSectionRepository.save(ItemSectionEntity(
                itemId = itemEntity.id,
                position = 2,
                header = "header",
                body = "body",
                star = true
        ))
    }

    @Test
    fun findByIdReturnsItem() {
        val item: Pair<ItemEntity, List<ItemSectionEntity>> = itemDomain.findById(itemEntity.id!!)
        Assertions.assertThat(item.second.size).isEqualTo(3)
        Assertions.assertThat(item.second.map { it.position }).isEqualTo(listOf(0, 1, 2))
    }

    @Test
    fun findByIdThrowsItemNotFoundException() {
        Assertions.assertThatThrownBy {
            itemDomain.findById(UUID.randomUUID().toString())
        }.isInstanceOf(ItemNotFoundException::class.java)
    }

    @Test
    fun scopeByUserIdCreateCreatesItem() {
        val itemEntity: ItemEntity = itemDomain.scopeByUserIdCreate(userEntity.id!!)
        Assertions.assertThat(itemRepository.findById(itemEntity.id!!)).isNotNull
    }

    @Test
    fun scopeByUserIdFindByIdReturnsItem() {
        val item: Pair<ItemEntity, List<ItemSectionEntity>> =
                itemDomain.scopeByUserIdFindById(userEntity.id!!, itemEntity.id!!)
        Assertions.assertThat(item.second.size).isEqualTo(3)
        Assertions.assertThat(item.second.map { it.position }).isEqualTo(listOf(0, 1, 2))
    }

    @Test
    fun scopeByUserIdFindByIdThrowsItemNotFoundException() {
        val otherUserEntity: UserEntity = userRepository.save(UserEntity(
                email = "${UUID.randomUUID()}@example.com",
                password = "password"
        ))

        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdFindById(userEntity.id!!, UUID.randomUUID().toString())
        }.isInstanceOf(ItemNotFoundException::class.java)

        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdFindById(otherUserEntity.id!!, itemEntity.id!!)
        }.isInstanceOf(ItemNotFoundException::class.java)
    }

    @Test
    fun scopeByUserIdModifyModifiesItem() {
        itemDomain.scopeByUserIdModify(userEntity.id!!,
                Pair(itemEntity.id!!, itemEntity.copy(title = "title(modified)")),
                listOf(Pair(itemSectionEntity0.id, itemSectionEntity0.copy(header = "header(modified)")),
                        Pair(itemSectionEntity2.id, itemSectionEntity2.copy(header = "header(modified)", position = 1)),
                        Pair(null, itemSectionEntity1.copy(header = "header(modified)", position = 2))))
        Assertions.assertThat(itemRepository.findById(itemEntity.id!!).get().title)
                .isEqualTo("title(modified)")
        Assertions.assertThat(itemSectionRepository.findById(itemSectionEntity0.id!!).get().header)
                .isEqualTo("header(modified)")
        Assertions.assertThat(itemSectionRepository.findById(itemSectionEntity1.id!!).isEmpty).isTrue()
    }

    @Test
    fun scopeByUserIdModifyThrowsItemModifyException() {
        val otherUserEntity: UserEntity = userRepository.save(UserEntity(
                email = "${UUID.randomUUID()}@example.com",
                password = "password"
        ))

        val otherItemEntity: ItemEntity = itemRepository.save(ItemEntity(
                userId = otherUserEntity.id,
                title = "title"
        ))

        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdModify(userEntity.id!!,
                    Pair(UUID.randomUUID().toString(), itemEntity.copy()),
                    listOf())
        }.isInstanceOf(ItemModifyException::class.java)

        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdModify(userEntity.id!!,
                    Pair(itemEntity.id!!, itemEntity.copy()),
                    listOf(Pair(UUID.randomUUID().toString(), itemSectionEntity0.copy())))
        }.isInstanceOf(ItemModifyException::class.java)

        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdModify(otherUserEntity.id!!,
                    Pair(itemEntity.id!!, itemEntity.copy()),
                    listOf(Pair(itemSectionEntity0.id, itemSectionEntity0.copy())))
        }.isInstanceOf(ItemModifyException::class.java)

        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdModify(otherUserEntity.id!!,
                    Pair(otherItemEntity.id!!, otherItemEntity),
                    listOf(Pair(itemSectionEntity0.id, itemSectionEntity0.copy())))
        }.isInstanceOf(ItemModifyException::class.java)
    }

}
