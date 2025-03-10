package writory.domain.item.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import writory.domain.item.entity.ItemSectionEntity

@Repository
interface ItemSectionRepository : JpaRepository<ItemSectionEntity, String> {

    fun findAllByItemId(itemId: String): List<ItemSectionEntity>

    fun findAllByItemIdOrderByPositionAsc(itemId: String): List<ItemSectionEntity>

}
