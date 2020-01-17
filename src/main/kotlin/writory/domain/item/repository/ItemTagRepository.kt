package writory.domain.item.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import writory.domain.item.entity.ItemTagEntity

@Repository
interface ItemTagRepository : JpaRepository<ItemTagEntity, String>
