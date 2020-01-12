package writory.domain.user

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import writory.domain.user.entity.UserEntity
import writory.domain.user.exception.PasswordMismatchException
import writory.domain.user.exception.UserFoundException
import writory.domain.user.exception.UserNotFoundException
import writory.domain.user.principal.UserPrincipal
import writory.domain.user.repository.UserRepository
import java.util.*

@SpringBootTest
class UserDomainTests {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userDomain: UserDomain

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var userEntity: UserEntity

    private lateinit var otherUserEntity: UserEntity

    @BeforeEach
    fun saves_entities() {
        userEntity = userRepository.save(UserEntity(
                email = "${UUID.randomUUID()}@example.com",
                password = passwordEncoder.encode("password")
        ))

        otherUserEntity = userRepository.save(UserEntity(
                email = "${UUID.randomUUID()}@example.com",
                password = passwordEncoder.encode("password")
        ))
    }

    @Test
    fun loadUserByUsername_returns_user() {
        val userPrincipal: UserPrincipal? = userDomain.loadUserByUsername(userEntity.email) as? UserPrincipal
        Assertions.assertThat(userPrincipal?.userEntity?.email).isEqualTo(userEntity.email)
    }

    @Test
    fun loadUserByUsername_throws_UsernameNotFoundException_when_user_does_not_exists() {
        Assertions.assertThatThrownBy {
            userDomain.loadUserByUsername("${UUID.randomUUID()}@example.com")
        }.isInstanceOf(UsernameNotFoundException::class.java)
    }

    @Test
    fun modifyEmail_modifies_email() {
        val email = "${UUID.randomUUID()}@example.com"
        userDomain.modifyEmail(userEntity.id!!, email)
        Assertions.assertThat(userRepository.findById(userEntity.id!!).get().email).isEqualTo(email)
    }

    @Test
    fun modifyEmail_throws_UserFoundException_when_email_already_used() {
        Assertions.assertThatThrownBy {
            userDomain.modifyEmail(userEntity.id!!, otherUserEntity.email!!)
        }.isInstanceOf(UserFoundException::class.java)
    }

    @Test
    fun modifyEmail_throws_UserNotFoundException_when_user_does_not_exists() {
        Assertions.assertThatThrownBy {
            userDomain.modifyEmail(UUID.randomUUID().toString(), "${UUID.randomUUID()}@example.com")
        }.isInstanceOf(UserNotFoundException::class.java)
    }

    @Test
    fun modifyPassword_modifies_password() {
        userDomain.modifyPassword(userEntity.id!!, "password", "modified")
        Assertions.assertThat(passwordEncoder.matches(
                "modified", userRepository.findById(userEntity.id!!).get().password!!)).isTrue()
    }

    @Test
    fun modifyPassword_throws_PasswordMismatchException_when_wrong_password_passed() {
        Assertions.assertThatThrownBy {
            userDomain.modifyPassword(userEntity.id!!, "wrongPassword", "modified")
        }.isInstanceOf(PasswordMismatchException::class.java)
    }

    @Test
    fun modifyPassword_throws_UserNotFoundException_when_user_does_not_exists() {
        Assertions.assertThatThrownBy {
            userDomain.modifyPassword(UUID.randomUUID().toString(), "password", "modified")
        }.isInstanceOf(UserNotFoundException::class.java)
    }

    @Test
    fun reloadUser_reloads_entity() {
        val principal: UserPrincipal = userDomain.loadUserByUsername(userEntity.email) as UserPrincipal

        val entity: UserEntity = userRepository.findByIdOrNull(userEntity.id)!!
        entity.email = "${UUID.randomUUID()}@example.com"
        userRepository.save(entity)

        userDomain.reloadUser(principal)

        Assertions.assertThat(principal.userEntity.email).isEqualTo(entity.email)
    }

    @Test
    fun signUp_creates_user() {
        val email = "${UUID.randomUUID()}@example.com"
        userDomain.signUp(email, "password")

        val newEntity: UserEntity? = userRepository.findByEmail(email)
        Assertions.assertThat(newEntity).isNotNull
        Assertions.assertThat(newEntity?.email).isEqualTo(email)
        Assertions.assertThat(passwordEncoder.matches("password", newEntity?.password)).isTrue()
    }

    @Test
    fun signUp_throws_UserFoundException_when_email_already_used() {
        Assertions.assertThatThrownBy {
            userDomain.signUp(userEntity.email!!, "password")
        }.isInstanceOf(UserFoundException::class.java)
    }

}
