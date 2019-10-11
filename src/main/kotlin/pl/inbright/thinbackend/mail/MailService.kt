package pl.inbright.thinbackend.mail

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MailService(val sendEmailQueue: Queue, val jmsTemplate: RabbitTemplate/*, val mailSender: JavaMailSender*/) {


    @Value("\${thinBackend.email-sender}")
    lateinit var sender: String

    fun send(mailMessage: MailMessage) {
        jmsTemplate.convertAndSend(sendEmailQueue.name, mailMessage)
    }

    @RabbitListener(queues = ["\${thinBackend.send-email-queue}"])
    private fun onSendMailMessage(message: MailMessage) {
        println(message.text)
    }

}