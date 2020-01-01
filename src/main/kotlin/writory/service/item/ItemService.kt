package writory.service.item

import writory.entity.ItemEntity
import writory.entity.ItemSectionEntity

interface ItemService {

    fun findItem(itemId: String): Pair<ItemEntity, List<ItemSectionEntity>>

}
