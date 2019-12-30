package writory.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import writory.entity.ItemSectionEntity

@Repository
interface ItemSectionRepository : JpaRepository<ItemSectionEntity, String>
