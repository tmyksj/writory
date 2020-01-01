package writory.service.dashboard

import writory.entity.ItemEntity
import writory.entity.ItemSectionEntity

interface DashboardService {

    fun createItem(userId: String): ItemEntity

    fun findItem(userId: String, itemId: String): Pair<ItemEntity, List<ItemSectionEntity>>

    fun modifyItem(userId: String,
                   item: Pair<String, ItemEntity>,
                   itemSectionList: List<Pair<String?, ItemSectionEntity>>)

}
