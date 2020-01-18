package writory.domain.item

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
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
    private lateinit var passwordEncoder: PasswordEncoder

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

    private lateinit var otherItemEntity: ItemEntity

    private lateinit var otherUserEntity: UserEntity

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

        otherUserEntity = userRepository.save(UserEntity(
                email = "${UUID.randomUUID()}@example.com",
                password = passwordEncoder.encode("password")
        ))

        otherItemEntity = itemRepository.save(ItemEntity(
                userId = otherUserEntity.id,
                title = "title"
        ))
    }

    @Test
    fun scopeByUserIdCreate_creates_item() {
        val itemEntity: ItemEntity = itemDomain.scopeByUserIdCreate(userEntity.id!!)
        Assertions.assertThat(itemRepository.findById(itemEntity.id!!)).isNotNull
    }

    @Test
    fun scopeByUserIdDeleteById_deletes_item() {
        itemDomain.scopeByUserIdDeleteById(userEntity.id!!, itemEntity.id!!)
        Assertions.assertThat(itemSectionRepository.findById(itemEntity.id!!).isEmpty).isTrue()
    }

    @Test
    fun scopeByUserIdDeleteById_throws_ItemNotFoundException_when_item_does_not_exists() {
        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdDeleteById(userEntity.id!!, UUID.randomUUID().toString())
        }.isInstanceOf(ItemNotFoundException::class.java)
    }

    @Test
    fun scopeByUserIdDeleteById_throws_ItemNotFoundException_when_other_user_tries_to_delete_item() {
        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdDeleteById(otherUserEntity.id!!, itemEntity.id!!)
        }.isInstanceOf(ItemNotFoundException::class.java)
    }

    @Test
    fun scopeByUserIdFindAllByUserId_returns_item_list() {
        Assertions.assertThat(itemDomain.scopeByUserIdFindAllByUserId(userEntity.id!!).size).isEqualTo(1)
    }

    @Test
    fun scopeByUserIdFindById_returns_item() {
        val item: Pair<ItemEntity, List<ItemSectionEntity>> =
                itemDomain.scopeByUserIdFindById(userEntity.id!!, itemEntity.id!!)
        Assertions.assertThat(item.second.size).isEqualTo(3)
        Assertions.assertThat(item.second.map { it.position }).isEqualTo(listOf(0, 1, 2))
    }

    @Test
    fun scopeByUserIdFindById_throws_ItemNotFoundException_when_item_does_not_exists() {
        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdFindById(userEntity.id!!, UUID.randomUUID().toString())
        }.isInstanceOf(ItemNotFoundException::class.java)
    }

    @Test
    fun scopeByUserIdFindById_throws_ItemNotFoundException_when_other_user_tries_to_find_item() {
        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdFindById(otherUserEntity.id!!, itemEntity.id!!)
        }.isInstanceOf(ItemNotFoundException::class.java)
    }

    @Test
    fun scopeByUserIdModify_modifies_item() {
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
    fun scopeByUserIdModify_throws_ItemModifyException_when_item_does_not_exists() {
        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdModify(userEntity.id!!,
                    Pair(UUID.randomUUID().toString(), itemEntity.copy()),
                    listOf())
        }.isInstanceOf(ItemModifyException::class.java)
    }

    @Test
    fun scopeByUserIdModify_throws_ItemModifyException_when_item_section_does_not_exists() {
        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdModify(userEntity.id!!,
                    Pair(itemEntity.id!!, itemEntity.copy()),
                    listOf(Pair(UUID.randomUUID().toString(), itemSectionEntity0.copy())))
        }.isInstanceOf(ItemModifyException::class.java)
    }

    @Test
    fun scopeByUserIdModify_throws_ItemModifyException_when_other_user_tries_to_modify_item() {
        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdModify(otherUserEntity.id!!,
                    Pair(itemEntity.id!!, itemEntity.copy()),
                    listOf(Pair(itemSectionEntity0.id, itemSectionEntity0.copy())))
        }.isInstanceOf(ItemModifyException::class.java)
    }

    @Test
    fun scopeByUserIdModify_throws_ItemModifyException_when_other_user_tries_to_modify_item_section() {
        Assertions.assertThatThrownBy {
            itemDomain.scopeByUserIdModify(otherUserEntity.id!!,
                    Pair(otherItemEntity.id!!, otherItemEntity),
                    listOf(Pair(itemSectionEntity0.id, itemSectionEntity0.copy())))
        }.isInstanceOf(ItemModifyException::class.java)
    }

}
