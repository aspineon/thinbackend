package pl.inbright.thinbackend.mail

import org.springframework.amqp.core.Queue
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmailConfiguration {

    @Bean
    fun sendEmailQueue(@Value("\${thinBackend.send-email-queue}") queueName: String) = Queue(queueName)

}