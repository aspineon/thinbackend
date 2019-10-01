package pl.inbright.thinbackend.security

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(private val authenticationManager: AuthenticationManager) : ServerSecurityContextRepository {

    private val tokenPrefix = "Bearer "
    private val emptyText = ""

    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        throw UnsupportedOperationException()
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        val tokenHeaderValue = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        val tokenValue = tokenHeaderValue?.replace(tokenPrefix, emptyText)
        if (tokenValue.isNullOrEmpty()) {
            return Mono.empty<SecurityContext>()
        }
        val authentication = UsernamePasswordAuthenticationToken(emptyText, tokenValue)
        return authenticationManager.authenticate(authentication)
                .map { SecurityContextImpl(it) }
    }

}