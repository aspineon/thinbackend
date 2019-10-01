package pl.inbright.thinbackend.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm.HMAC256
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager(@Value("\${thinBackend.jwtSecret}") private val jwtSecret: String) : ReactiveAuthenticationManager {

    private val userClaimName = "user"
    private val rolesClaimName = "roles"
    private val verifier: JWTVerifier = JWT.require(HMAC256(jwtSecret)).build()

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return try {
            val token = authentication.credentials.toString()
            val jwtToken = verifier.verify(token)
            val claims = jwtToken.claims
            if (!claims.containsKey(userClaimName) || !claims.containsKey(rolesClaimName)) {
                throw JWTVerificationException("Missing required claims")
            }
            val principal = claims.getValue(userClaimName).asString()
            val authorities = claims.getValue(rolesClaimName).asArray(String::class.java)
                    .map { SimpleGrantedAuthority(it) }
            val authenticationToken = UsernamePasswordAuthenticationToken(principal, token, authorities)
            Mono.just<Authentication>(authenticationToken)
        } catch (exception: JWTVerificationException) {
            Mono.empty<Authentication>()
        }
    }

}