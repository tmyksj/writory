package writory.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import writory.principal.UserPrincipalService

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration(
        private val userPrincipalService: UserPrincipalService
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/sign-in").permitAll()
                .antMatchers("/sign-up").permitAll()
                .anyRequest().authenticated()
        http.formLogin()
                .loginPage("/sign-in")
                .loginProcessingUrl("/sign-in")
                .defaultSuccessUrl("/dashboard")
                .failureUrl("/sign-in")
                .usernameParameter("email")
                .passwordParameter("password")
        http.logout()
                .logoutUrl("/sign-out")
                .logoutSuccessUrl("/")
        http.userDetailsService(userPrincipalService)
    }

}
