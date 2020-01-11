package writory.domain.legal.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import writory.domain.legal.LegalDomain
import writory.domain.legal.repository.PrivacyPolicyRepository
import writory.domain.legal.repository.TermsOfServiceRepository

@Component
@Transactional(propagation = Propagation.REQUIRED)
class LegalDomainImpl(
        private val privacyPolicyRepository: PrivacyPolicyRepository,
        private val termsOfServiceRepository: TermsOfServiceRepository
) : LegalDomain {

    override fun findPrivacyPolicy(): String {
        return privacyPolicyRepository.findFirstByOrderByModifiedDesc()?.body ?: "lorem ipsum"
    }

    override fun findTermsOfService(): String {
        return termsOfServiceRepository.findFirstByOrderByModifiedDesc()?.body ?: "lorem ipsum"
    }

}
