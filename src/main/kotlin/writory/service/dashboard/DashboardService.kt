package writory.service.dashboard

import writory.domain.user.principal.UserPrincipal

interface DashboardService {

    fun modifyEmail(userPrincipal: UserPrincipal, email: String)

    fun modifyPassword(userPrincipal: UserPrincipal, currentPasswordRaw: String, newPasswordRaw: String)

}
