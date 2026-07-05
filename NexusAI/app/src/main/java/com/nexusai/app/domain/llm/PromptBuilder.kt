package com.nexusai.app.domain.llm

object PromptBuilder {

    fun build(
        perfilPrompt: String,
        ragContext: String,
        webContext: String,
        userMessage: String,
        historial: String
    ): String {
        return buildString {
            appendLine(perfilPrompt)
            appendLine()

            if (ragContext.isNotBlank()) {
                appendLine("[CONTEXTO RAG LOCAL]")
                appendLine(ragContext)
                appendLine()
            }

            if (webContext.isNotBlank()) {
                appendLine("[INFORMACIÓN RECIENTE DE INTERNET - Utilízala de forma resumida]")
                appendLine(webContext)
                appendLine()
            }

            if (historial.isNotBlank()) {
                appendLine("[HISTORIAL RECIENTE]")
                appendLine(historial)
                appendLine()
            }

            appendLine("[MENSAJE DEL USUARIO]")
            appendLine("Usuario: $userMessage")
            appendLine()
            appendLine("Respuesta:")
        }
    }

    fun buildForLegacyAvatar(
        nombrePersona: String,
        fragmentosMemoria: String,
        perfilPrompt: String,
        userMessage: String,
        historial: String
    ): String {
        return buildString {
            appendLine("[IDENTIDAD DEL COMPAÑERO]")
            appendLine("Actúas como $nombrePersona. Hablas en primera persona.")
            appendLine("Tu temperamento, opiniones espirituales, puntos de vista políticos y modismos lingüísticos deben basarse estrictamente en la esencia y filosofía reflejada en los fragmentos de tus memorias adjuntos a continuación.")
            appendLine()
            appendLine("REGLA CLAVE DE INTERACCIÓN: Prioriza reflejar fielmente el carácter, el tono y la forma de ver la vida de la persona descrita en los fragmentos. Si el usuario te pregunta por datos concretos o históricos que no constan en el texto provisto, puedes deducir respuestas creativas o imaginativas que coincidan con tus opiniones y carácter, pero bajo ninguna circunstancia comprometas tu estilo de hablar o tu ética descrita.")
            appendLine()

            if (fragmentosMemoria.isNotBlank()) {
                appendLine("[FRAGMENTOS DE MEMORIAS RECUPERADOS DEL RAG LOCAL]")
                appendLine(fragmentosMemoria)
                appendLine()
            }

            if (historial.isNotBlank()) {
                appendLine("[HISTORIAL RECIENTE]")
                appendLine(historial)
                appendLine()
            }

            appendLine("[MENSAJE DEL USUARIO]")
            appendLine("Usuario: $userMessage")
            appendLine()
            appendLine("Respuesta (En primera persona, adoptando el carácter y el tono del legado):")
        }
    }
}
