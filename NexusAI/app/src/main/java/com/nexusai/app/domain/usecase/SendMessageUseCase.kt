package com.nexusai.app.domain.usecase

import com.nexusai.app.ai.rag.RAGRetriever
import com.nexusai.app.ai.security.ConstitutionalGuard
import com.nexusai.app.ai.web.BuscadorWebLocal
import com.nexusai.app.data.repository.ChatRepository
import com.nexusai.app.data.repository.PerfilRepository
import com.nexusai.app.domain.llm.LocalInferenceEngine
import kotlinx.coroutines.runBlocking

class SendMessageUseCase(
    private val chatRepository: ChatRepository,
    private val perfilRepository: PerfilRepository,
    private val inferenceEngine: LocalInferenceEngine,
    private val ragRetriever: RAGRetriever,
    private val buscadorWeb: BuscadorWebLocal,
    private val guard: ConstitutionalGuard
) {

    suspend operator fun invoke(
        perfilId: Long,
        mensaje: String,
        enableWebSearch: Boolean = false,
        onResponse: (String) -> Unit
    ) {
        val perfil = perfilRepository.getPerfilById(perfilId) ?: return

        chatRepository.sendMessage(perfilId, "USUARIO", mensaje)

        val ragContext = ragRetriever.retrieveAsContext(perfilId, mensaje)

        val webContext = if (enableWebSearch) {
            buscadorWeb.buscarComoContexto(mensaje)
        } else ""

        val historial = chatRepository.getRecentMensajes(perfilId, 6)
            .joinToString("\n") { "${it.remitente}: ${it.contenido}" }

        val result = inferenceEngine.generateResponse(
            prompt = mensaje,
            tipoPerfil = perfil.tipo,
            ragContext = ragContext,
            webContext = webContext,
            callback = { response ->
                runBlocking {
                    chatRepository.sendMessage(perfilId, "IA", response)
                }
                onResponse(response)
            }
        )

        result.onFailure { error ->
            val errorMsg = "Error al procesar el mensaje: ${error.message}"
            chatRepository.sendMessage(perfilId, "IA", errorMsg)
            onResponse(errorMsg)
        }
    }
}
