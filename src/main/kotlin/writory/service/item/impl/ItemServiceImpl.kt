package writory.service.item.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import writory.entity.ItemEntity
import writory.entity.ItemSectionEntity
import writory.exception.item.ItemNotFoundException
import writory.repository.ItemRepository
import writory.repository.ItemSectionRepository
import writory.service.item.ItemService

@Service
@Transactional
class ItemServiceImpl(
        private val itemRepository: ItemRepository,
        private val itemSectionRepository: ItemSectionRepository
) : ItemService {

    override fun findItem(itemId: String): Pair<ItemEntity, List<ItemSectionEntity>> {
        val entity: ItemEntity = itemRepository.findById(itemId).orElse(null) ?: throw ItemNotFoundException()
        val sectionEntityList: List<ItemSectionEntity> = itemSectionRepository.findAllByItemId(itemId)

        return Pair(entity, sectionEntityList)
    }

}
