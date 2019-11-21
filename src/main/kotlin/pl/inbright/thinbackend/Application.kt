package pl.inbright.thinbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@SpringBootApplication
class Application() {

    @Bean
    fun routes() = RouterFunctions.route(GET("/api/v1/messages"), HandlerFunction {
        ServerResponse.ok().build();
    })

}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}