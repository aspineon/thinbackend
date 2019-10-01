package pl.inbright.thinbackend.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User.withUsername
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
class SecurityConfiguration(private val securityContextRepository: SecurityContextRepository, private val authenticationManager: AuthenticationManager) {

    enum class ROLE {
        ADMIN
    }

    @Bean
    fun configure(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .securityContextRepository(securityContextRepository)
                .authenticationManager(authenticationManager)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(HttpMethod.POST, "/credentials").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/**").permitAll()
                .anyExchange().authenticated()
                .and().build();
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder();

    @Bean
    fun userDetailsService(@Value("\${thinBackend.adminUsername}") username: String,
                           @Value("\${thinBackend.adminPassword}") password: String,
                           passwordEncoder: PasswordEncoder): MapReactiveUserDetailsService {
        val user = withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles(ROLE.ADMIN.name).build()
        return MapReactiveUserDetailsService(user)
    }

}