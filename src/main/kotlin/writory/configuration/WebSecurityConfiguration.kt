package writory.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import writory.domain.user.UserDomain

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration(
        private val userDomain: UserDomain
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/about").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/item/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/privacy-policy").permitAll()
                .antMatchers("/sign-in").permitAll()
                .antMatchers("/sign-up").permitAll()
                .antMatchers("/terms-of-service").permitAll()
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
        http.userDetailsService(userDomain)
    }

}
