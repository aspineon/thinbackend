package pl.inbright.thinbackend.common

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.*

@Service
class TextSource(val messageSource: MessageSource, @Qualifier("templateEngine") val templateEngine: TemplateEngine) {

    val languageSeparator = "_"

    fun getText(key: String, language: String) = messageSource.getMessage(key, null, Locale(language))

    fun getTextFromTemplate(templateName: String, variables: Map<String, Any>, language: String) = evaluateTemplate(templateName, variables, language)

    private fun evaluateTemplate(templateBaseName: String, variables: Map<String, Any>, language: String): String {
        val context = Context()
        context.setVariables(variables)
        val templateName = templateBaseName + languageSeparator + language
        return templateEngine.process(templateName, context)
    }

}