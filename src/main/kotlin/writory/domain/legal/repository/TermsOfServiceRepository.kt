package writory.domain.legal.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import writory.domain.legal.entity.TermsOfServiceEntity

@Repository
interface TermsOfServiceRepository : JpaRepository<TermsOfServiceEntity, String> {

    fun findFirstByOrderByModifiedDesc(): TermsOfServiceEntity?

}
