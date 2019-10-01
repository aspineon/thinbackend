package pl.inbright.thinbackend.security

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
class LoginController(val securityService: SecurityService) {

    @RequestMapping(value = ["/credentials"], method = [RequestMethod.POST])
    fun login(@RequestBody credentials: Credentials): Mono<ResponseEntity<Token>> {
        return securityService.login(credentials)
                .map { ResponseEntity.ok(it) }
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun onBadCredentials() = Mono.just<ResponseEntity<Any>>(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())

}