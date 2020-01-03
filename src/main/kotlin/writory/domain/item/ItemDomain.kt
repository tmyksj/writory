package writory.domain.item

import writory.domain.item.entity.ItemEntity
import writory.domain.item.entity.ItemSectionEntity

interface ItemDomain {

    fun create(userId: String): ItemEntity

    fun find(itemId: String): Pair<ItemEntity, List<ItemSectionEntity>>

    fun find(userId: String, itemId: String): Pair<ItemEntity, List<ItemSectionEntity>>

    fun modify(userId: String, item: Pair<String, ItemEntity>, itemSectionList: List<Pair<String?, ItemSectionEntity>>)

}
