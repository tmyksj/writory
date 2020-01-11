package writory.domain.legal

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LegalDomainTests {

    @Autowired
    private lateinit var legalDomain: LegalDomain

    @Test
    fun findPrivacyPolicy_returns_privacy_policy() {
        Assertions.assertThat(legalDomain.findPrivacyPolicy()).isNotBlank()
    }

    @Test
    fun findTermsOfService_returns_terms_of_service() {
        Assertions.assertThat(legalDomain.findTermsOfService()).isNotBlank()
    }

}
