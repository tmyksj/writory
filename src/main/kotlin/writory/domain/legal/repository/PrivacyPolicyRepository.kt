package writory.domain.legal.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import writory.domain.legal.entity.PrivacyPolicyEntity

@Repository
interface PrivacyPolicyRepository : JpaRepository<PrivacyPolicyEntity, String> {

    fun findFirstByOrderByModifiedDesc(): PrivacyPolicyEntity?

}
