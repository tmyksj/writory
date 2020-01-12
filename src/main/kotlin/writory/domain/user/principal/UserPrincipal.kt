package writory.domain.user.principal

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import writory.domain.user.entity.UserEntity

class UserPrincipal(
        var userEntity: UserEntity
) : UserDetails {

    val id: String = userEntity.id!!

    private val accountNonExpired: Boolean = true

    private val accountNonLocked: Boolean = true

    private val authorities: MutableCollection<out GrantedAuthority> =
            mutableListOf(SimpleGrantedAuthority("ROLE_USER"))

    private val credentialsNonExpired: Boolean = true

    private val enabled: Boolean = true

    private val password: String = userEntity.password!!

    private val username: String = userEntity.email!!

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return accountNonExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return accountNonLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return credentialsNonExpired
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

}
