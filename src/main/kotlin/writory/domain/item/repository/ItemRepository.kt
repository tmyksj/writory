package writory.domain.item.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import writory.domain.item.entity.ItemEntity

@Repository
interface ItemRepository : JpaRepository<ItemEntity, String>
