package writory.service.dashboard.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import writory.domain.user.UserDomain
import writory.domain.user.principal.UserPrincipal
import writory.service.dashboard.DashboardService
import writory.service.dashboard.exception.PasswordMismatchException
import writory.service.dashboard.exception.UserFoundException

@Component
@Transactional(propagation = Propagation.REQUIRED)
class DashboardServiceImpl(
        private val userDomain: UserDomain
) : DashboardService {

    override fun modifyEmail(userPrincipal: UserPrincipal, email: String) {
        try {
            userDomain.modifyEmail(userPrincipal.userEntity.id!!, email)
            userDomain.reloadUser(userPrincipal)
        } catch (e: writory.domain.user.exception.UserFoundException) {
            throw UserFoundException()
        }
    }

    override fun modifyPassword(userPrincipal: UserPrincipal, currentPasswordRaw: String, newPasswordRaw: String) {
        try {
            userDomain.modifyPassword(userPrincipal.userEntity.id!!, currentPasswordRaw, newPasswordRaw)
            userDomain.reloadUser(userPrincipal)
        } catch (e: writory.domain.user.exception.PasswordMismatchException) {
            throw PasswordMismatchException()
        }
    }

}
