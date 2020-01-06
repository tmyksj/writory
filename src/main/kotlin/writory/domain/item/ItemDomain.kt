package writory.domain.item

import writory.domain.item.entity.ItemEntity
import writory.domain.item.entity.ItemSectionEntity

interface ItemDomain {

    fun findById(itemId: String): Pair<ItemEntity, List<ItemSectionEntity>>

    fun scopeByUserIdCreate(userId: String): ItemEntity

    fun scopeByUserIdFindAllByUserId(userId: String): List<ItemEntity>

    fun scopeByUserIdFindById(userId: String, itemId: String): Pair<ItemEntity, List<ItemSectionEntity>>

    fun scopeByUserIdModify(userId: String,
                            item: Pair<String, ItemEntity>,
                            itemSectionList: List<Pair<String?, ItemSectionEntity>>)

}
