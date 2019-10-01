package pl.inbright.thinbackend.mail

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.MimeMessagePreparator
import org.springframework.stereotype.Service
import java.util.logging.Level
import java.util.logging.Logger

@Service
class MailService(val sendEmailQueue: Queue, val jmsTemplate: RabbitTemplate, val mailSender: JavaMailSender) {

    @Value("\${thinBackend.email-sender}")
    lateinit var sender: String

    fun send(mailMessage: MailMessage) {
        jmsTemplate.convertAndSend(sendEmailQueue.name, mailMessage)
    }

    @RabbitListener(queues = ["\${thinBackend.send-email-queue}"])
    private fun onSendMailMessage(message: MailMessage) {
        val messagePreparator = createMimeMessagePreparator(message)
        try {
            mailSender.send(messagePreparator)
        } catch (exception: MailException) {
            Logger.getLogger(MailService::class.java.name).log(Level.WARNING, "Error sending email: " + exception.message)
        }
    }

    private fun createMimeMessagePreparator(message: MailMessage): MimeMessagePreparator {
        return MimeMessagePreparator { mimeMessage ->
            val messageHelper = MimeMessageHelper(mimeMessage)
            messageHelper.setFrom(sender)
            messageHelper.setTo(message.recipient)
            messageHelper.setSubject(message.subject)
            messageHelper.setText(message.text, true)
        }
    }

}