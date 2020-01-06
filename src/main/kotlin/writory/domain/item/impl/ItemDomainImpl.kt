package writory.domain.item.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import writory.domain.item.ItemDomain
import writory.domain.item.entity.ItemEntity
import writory.domain.item.entity.ItemSectionEntity
import writory.domain.item.exception.ItemModifyException
import writory.domain.item.exception.ItemNotFoundException
import writory.domain.item.repository.ItemRepository
import writory.domain.item.repository.ItemSectionRepository

@Component
@Transactional(propagation = Propagation.REQUIRED)
class ItemDomainImpl(
        private val itemRepository: ItemRepository,
        private val itemSectionRepository: ItemSectionRepository
) : ItemDomain {

    override fun findById(itemId: String): Pair<ItemEntity, List<ItemSectionEntity>> {
        val entity: ItemEntity = itemRepository.findById(itemId).orElse(null) ?: throw ItemNotFoundException()
        val sectionEntityList: List<ItemSectionEntity> = itemSectionRepository.findAllByItemIdOrderByPositionAsc(itemId)

        return Pair(entity, sectionEntityList)
    }

    override fun scopeByUserIdCreate(userId: String): ItemEntity {
        return itemRepository.save(ItemEntity(
                userId = userId,
                title = ""
        ))
    }

    override fun scopeByUserIdFindAllByUserId(userId: String): List<ItemEntity> {
        return itemRepository.findAllByUserIdOrderByModifiedDesc(userId)
    }

    override fun scopeByUserIdFindById(userId: String, itemId: String): Pair<ItemEntity, List<ItemSectionEntity>> {
        val entity: ItemEntity = itemRepository.findById(itemId).orElse(null) ?: throw ItemNotFoundException()
        val sectionEntityList: List<ItemSectionEntity> = itemSectionRepository.findAllByItemIdOrderByPositionAsc(itemId)

        if (userId != entity.userId) {
            throw ItemNotFoundException()
        }

        return Pair(entity, sectionEntityList)
    }

    override fun scopeByUserIdModify(userId: String,
                                     item: Pair<String, ItemEntity>,
                                     itemSectionList: List<Pair<String?, ItemSectionEntity>>) {
        if (itemSectionList.map { it.second.position }.toSet().size != itemSectionList.size) {
            throw ItemModifyException()
        }

        val createSection: List<ItemSectionEntity> = itemSectionList.filter { it.first == null }.map { it.second }
        val modifySection: Map<String?, ItemSectionEntity> = itemSectionList.filter { it.first != null }.toMap()

        val entity: ItemEntity = itemRepository.findById(item.first).orElse(null) ?: throw ItemModifyException()
        if (userId != entity.userId) {
            throw ItemModifyException()
        }

        val sectionEntityList: List<ItemSectionEntity> = itemSectionRepository.findAllByItemId(item.first)
        val modifySectionEntity: List<ItemSectionEntity> =
                sectionEntityList.filter { modifySection.containsKey(it.id) }
        val deleteSectionEntity: List<ItemSectionEntity> =
                sectionEntityList.filter { !modifySection.containsKey(it.id) }

        if (modifySection.size != modifySectionEntity.size) {
            throw ItemModifyException()
        }

        entity.apply {
            title = item.second.title
        }

        itemSectionRepository.saveAll(createSection.map {
            ItemSectionEntity(
                    itemId = item.first,
                    position = it.position,
                    header = it.header,
                    body = it.body,
                    star = it.star
            )
        })

        modifySectionEntity.forEach {
            it.apply {
                position = modifySection[it.id]?.position
                header = modifySection[it.id]?.header
                body = modifySection[it.id]?.body
                star = modifySection[it.id]?.star
            }
        }

        itemSectionRepository.deleteAll(deleteSectionEntity)
    }

}
