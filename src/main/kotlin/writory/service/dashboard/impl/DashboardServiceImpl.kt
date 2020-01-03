package writory.service.dashboard.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import writory.entity.ItemEntity
import writory.entity.ItemSectionEntity
import writory.exception.dashboard.ItemModifyException
import writory.exception.dashboard.ItemNotFoundException
import writory.exception.dashboard.ItemPermissionException
import writory.repository.ItemRepository
import writory.repository.ItemSectionRepository
import writory.service.dashboard.DashboardService

@Service
@Transactional
class DashboardServiceImpl(
        private val itemRepository: ItemRepository,
        private val itemSectionRepository: ItemSectionRepository
) : DashboardService {

    override fun createItem(userId: String): ItemEntity {
        return itemRepository.save(ItemEntity(
                userId = userId,
                title = ""
        ))
    }

    override fun findItem(userId: String, itemId: String): Pair<ItemEntity, List<ItemSectionEntity>> {
        val entity: ItemEntity = itemRepository.findById(itemId).orElse(null) ?: throw ItemNotFoundException()
        val sectionEntityList: List<ItemSectionEntity> = itemSectionRepository.findAllByItemIdOrderByPositionAsc(itemId)

        if (userId != entity.userId) {
            throw ItemPermissionException()
        }

        return Pair(entity, sectionEntityList)
    }

    override fun modifyItem(userId: String,
                            item: Pair<String, ItemEntity>,
                            itemSectionList: List<Pair<String?, ItemSectionEntity>>) {
        val entity: ItemEntity = itemRepository.findById(item.first).orElse(null) ?: throw ItemModifyException()
        val sectionEntityList: List<ItemSectionEntity> = itemSectionRepository.findAllByItemId(item.first)

        if (userId != entity.userId) {
            throw ItemModifyException()
        }

        val createSection: List<ItemSectionEntity> = itemSectionList.filter { it.first == null }.map { it.second }
        val modifySection: Map<String?, ItemSectionEntity> = itemSectionList.filter { it.first != null }.toMap()

        val modifySectionEntity: List<ItemSectionEntity> =
                sectionEntityList.filter { modifySection.containsKey(it.id) }
        val deleteSectionEntity: List<ItemSectionEntity> =
                sectionEntityList.filter { !modifySection.containsKey(it.id) }

        if (itemSectionList.map { it.second.position }.toSet().size != itemSectionList.size) {
            throw ItemModifyException()
        }

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
