package com.nexusai.app.domain.llm

import android.content.Context
import android.util.Log
import com.nexusai.app.R

/**
 * Constructor dinámico de prompts del sistema.
 *
 * Resuelve los textos de personalidad y la Directiva DUDH desde los recursos
 * de Android (strings.xml), lo que permite la traducción automática al idioma
 * activo del dispositivo sin modificar una línea de código Kotlin.
 *
 * Uso:
 *   PromptBuilder(context).build(tipoPerfil = "AMIGO", ...)
 */
class PromptBuilder(private val context: Context) {

    companion object {
        private const val TAG = "NexusAI_PromptBuilder"
    }

    /**
     * Mapeo de tipos de perfil a identificadores de recurso string.
     * Cada clave (ej. "AMIGO") corresponde a R.string.prompt_amigo.
     */
    private val mapaRecursosPerfil = mapOf(
        "AMIGO" to R.string.prompt_amigo,
        "JEFE" to R.string.prompt_jefe,
        "ANALISTA" to R.string.prompt_analista,
        "ANIMADOR" to R.string.prompt_animador,
        "PSICOLOGO" to R.string.prompt_psicologo,
        "PAREJA" to R.string.prompt_pareja,
        "CRISTIANO" to R.string.prompt_cristiano,
        "MUSULMAN" to R.string.prompt_musulman,
        "BUDISTA" to R.string.prompt_budista,
        "RABINO" to R.string.prompt_rabino,
        "HINDUISTA" to R.string.prompt_hinduista,
        "ESTOICO" to R.string.prompt_estoico,
        "ENTRENADOR" to R.string.prompt_entrenador,
    )

    /**
     * Resuelve el prompt de personalidad desde strings.xml según el tipo de perfil.
     * @param tipoPerfil Código del perfil (ej. "AMIGO", "JEFE", "LEGACY_AVATAR")
     * @return String del prompt en el idioma activo del dispositivo
     */
    fun resolverPromptPerfil(tipoPerfil: String): String {
        val recursoId = mapaRecursosPerfil[tipoPerfil.uppercase()]
        if (recursoId != null) {
            val prompt = context.getString(recursoId)
            Log.d(TAG, "Prompt resuelto desde resources para perfil: $tipoPerfil")
            return prompt
        }
        Log.w(TAG, "Tipo de perfil no reconocido: $tipoPerfil — usando prompt legacy avatar")
        return context.getString(R.string.prompt_legacy_avatar)
    }

    /**
     * Lee la Directiva Constitucional DUDH desde strings.xml.
     * Android selecciona automáticamente el idioma configurado por el usuario.
     */
    fun resolverDirectivaDUDH(): String {
        return context.getString(R.string.dudh_directive)
    }

    /**
     * Construye el prompt completo para el LLM.
     *
     * @param tipoPerfil  Código del perfil para resolver su prompt de personalidad
     * @param ragContext  Fragmentos relevantes recuperados del RAG
     * @param webContext  Resultados de búsqueda web (si aplica)
     * @param userMessage Mensaje del usuario
     * @param historial   Últimos mensajes del historial de la conversación
     * @param directivaDUDH Texto de la Directiva Constitucional (opcional, se auto-resuelve)
     * @return Prompt completo listo para enviar al motor de inferencia
     */
    fun build(
        tipoPerfil: String,
        ragContext: String = "",
        webContext: String = "",
        userMessage: String = "",
        historial: String = "",
        directivaDUDH: String = resolverDirectivaDUDH()
    ): String {
        val perfilPrompt = resolverPromptPerfil(tipoPerfil)
        val promptConDirectiva = "$directivaDUDH\n\n$perfilPrompt"

        return buildString {
            appendLine(promptConDirectiva)
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

    /**
     * Construye el prompt para avatares personalizados (legacy).
     * Usa el nombre y fragmentos de memoria en lugar de un tipo predefinido.
     *
     * @param nombrePersona    Nombre del avatar/persona
     * @param fragmentosMemoria Fragmentos RAG del documento cargado
     * @param tipoPerfil        Tipo de perfil (por defecto LEGACY_AVATAR)
     * @param userMessage       Mensaje del usuario
     * @param historial         Últimos mensajes de la conversación
     * @param directivaDUDH     Texto de la Directiva Constitucional
     * @return Prompt completo para el avatar legacy
     */
    fun buildForLegacyAvatar(
        nombrePersona: String,
        fragmentosMemoria: String,
        tipoPerfil: String = "LEGACY_AVATAR",
        userMessage: String = "",
        historial: String = "",
        directivaDUDH: String = resolverDirectivaDUDH()
    ): String {
        val perfilPrompt = resolverPromptPerfil(tipoPerfil)
        val promptConDirectiva = "$directivaDUDH\n\n$perfilPrompt"

        return buildString {
            appendLine(promptConDirectiva)
            appendLine()
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
