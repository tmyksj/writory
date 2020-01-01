package writory.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import writory.entity.ItemEntity

@Repository
interface ItemRepository : JpaRepository<ItemEntity, String>
