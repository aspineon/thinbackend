package pl.inbright.thinbackend.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC256
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class SecurityService(private val userDetailsService: MapReactiveUserDetailsService,
                      private val passwordEncoder: PasswordEncoder,
                      @Value("\${thinBackend.jwtSecret}") private val jwtSecret: String,
                      @Value("\${thinBackend.jwtExpirationTimeInSeconds}") private val jwtExpirationTime: Long) {

    private val userClaimName = "user"
    private val rolesClaimName = "roles"

    fun login(credentials: Credentials): Mono<Token> {
        return userDetailsService.findByUsername(credentials.username)
                .map {
                    val expirationDate = createExpirationDate()
                    if (passwordEncoder.matches(credentials.password, it.password)) {
                        Token(createToken(credentials, it, expirationDate), expirationDate.time)
                    } else {
                        throw BadCredentialsException("Bad credentials")
                    }
                }
    }

    private fun createToken(credentials: Credentials, userDetails: UserDetails, expirationDate: Date): String {
        val roles = userDetails.authorities.map { it.authority }.toTypedArray()
        return JWT.create()
                .withClaim(userClaimName, credentials.username)
                .withArrayClaim(rolesClaimName, roles)
                .withExpiresAt(expirationDate)
                .sign(HMAC256(jwtSecret))
    }

    private fun createExpirationDate(): Date {
        val expirationDateTime = LocalDateTime.now().plusSeconds(jwtExpirationTime)
        return Date.from(expirationDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant())
    }

}