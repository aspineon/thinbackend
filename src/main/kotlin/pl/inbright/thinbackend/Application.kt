package pl.inbright.thinbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import pl.inbright.thinbackend.mail.MailMessage
import pl.inbright.thinbackend.mail.MailService
import javax.annotation.PostConstruct

@SpringBootApplication
class Application(val mailService: MailService) {

    @PostConstruct
    fun init() {
        val message = MailMessage()
        message.text = "Hello"
        mailService.send(message)
    }

}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}