package writory.domain.item

import writory.domain.item.entity.ItemEntity
import writory.domain.item.entity.ItemSectionEntity

interface ItemDomain {

    fun findById(itemId: String): Pair<ItemEntity, List<ItemSectionEntity>>

    fun withUserIdCreate(userId: String): ItemEntity

    fun withUserIdFindAllByUserId(userId: String): List<ItemEntity>

    fun withUserIdFindById(userId: String, itemId: String): Pair<ItemEntity, List<ItemSectionEntity>>

    fun withUserIdModify(userId: String,
                         item: Pair<String, ItemEntity>,
                         itemSectionList: List<Pair<String?, ItemSectionEntity>>)

}
