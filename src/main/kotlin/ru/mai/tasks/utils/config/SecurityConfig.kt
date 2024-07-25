package ru.mai.tasks.utils.config

import ru.mai.tasks.utils.token.TokenFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    @Autowired private val tokenFilter: TokenFilter
) {
    val permittedRoutes = arrayOf("", "/*", "/**", "/links/*", "/.well-known/*", "/static/**", "/images/**", "/js/**", "/css/**", "/api/v1/health*", "/api/v1/auth/signin", "/api/v1/auth/refresh")

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeHttpRequests().requestMatchers(*permittedRoutes).permitAll()
            .anyRequest().authenticated().and()
            .addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}