package com.nexusai.app.ui.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nexusai.app.ai.rag.EmbeddingEngine
import com.nexusai.app.ai.rag.RAGRetriever
import com.nexusai.app.ai.security.ConstitutionalGuard
import com.nexusai.app.ai.web.BuscadorWebLocal
import com.nexusai.app.data.model.MensajeChat
import com.nexusai.app.data.model.PerfilIA
import com.nexusai.app.data.repository.ChatRepository
import com.nexusai.app.data.repository.PerfilRepository
import com.nexusai.app.domain.llm.LocalInferenceEngine
import com.nexusai.app.domain.llm.PromptBuilder
import com.nexusai.app.domain.usecase.ExtractGustosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatUiState(
    val mensajes: List<MensajeChat> = emptyList(),
    val perfil: PerfilIA? = null,
    val isLoading: Boolean = false,
    val isWebSearchEnabled: Boolean = false
)

class ChatViewModel(
    private val perfilId: Long,
    private val perfilRepository: PerfilRepository,
    private val chatRepository: ChatRepository,
    private val inferenceEngine: LocalInferenceEngine,
    private val ragRetriever: RAGRetriever,
    private val buscadorWeb: BuscadorWebLocal,
    private val extractGustosUseCase: ExtractGustosUseCase,
    private val guard: ConstitutionalGuard
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            perfilRepository.getPerfilById(perfilId)?.let { perfil ->
                _uiState.value = _uiState.value.copy(perfil = perfil)
            }
        }

        viewModelScope.launch {
            chatRepository.getMensajesByPerfil(perfilId).collect { mensajes ->
                _uiState.value = _uiState.value.copy(mensajes = mensajes)
            }
        }
    }

    fun toggleWebSearch() {
        _uiState.value = _uiState.value.copy(
            isWebSearchEnabled = !_uiState.value.isWebSearchEnabled
        )
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || _uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val perfil = _uiState.value.perfil ?: return@launch

            chatRepository.sendMessage(perfilId, "USUARIO", text)

            val ragContext = ragRetriever.retrieveAsContext(perfilId, text)

            val webContext = if (_uiState.value.isWebSearchEnabled) {
                buscadorWeb.buscarComoContexto(text)
            } else ""

            val historial = _uiState.value.mensajes
                .takeLast(6)
                .joinToString("\n") { "${it.remitente}: ${it.contenido}" }

            val result = inferenceEngine.generateResponse(
                prompt = text,
                perfilPrompt = perfil.promptSistemaBase,
                ragContext = ragContext,
                webContext = webContext,
                callback = { response ->
                    viewModelScope.launch {
                        chatRepository.sendMessage(perfilId, "IA", response)
                        extractGustosUseCase(text)
                    }
                }
            )

            result.onFailure { error ->
                chatRepository.sendMessage(perfilId, "IA", "Error: ${error.message}")
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    class Factory(
        private val perfilId: Long,
        private val perfilRepository: PerfilRepository,
        private val chatRepository: ChatRepository,
        private val inferenceEngine: LocalInferenceEngine,
        private val ragRetriever: RAGRetriever,
        private val buscadorWeb: BuscadorWebLocal,
        private val extractGustosUseCase: ExtractGustosUseCase,
        private val guard: ConstitutionalGuard
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatViewModel(
                perfilId = perfilId,
                perfilRepository = perfilRepository,
                chatRepository = chatRepository,
                inferenceEngine = inferenceEngine,
                ragRetriever = ragRetriever,
                buscadorWeb = buscadorWeb,
                extractGustosUseCase = extractGustosUseCase,
                guard = guard
            ) as T
        }
    }
}
