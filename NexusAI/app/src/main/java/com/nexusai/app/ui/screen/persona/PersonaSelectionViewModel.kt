package com.nexusai.app.ui.screen.persona

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nexusai.app.data.model.PerfilIA
import com.nexusai.app.data.repository.PerfilRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PersonaSelectionViewModel(
    private val perfilRepository: PerfilRepository
) : ViewModel() {

    val perfiles: StateFlow<List<PerfilIA>> = perfilRepository.getAllPerfiles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val activePerfil: StateFlow<PerfilIA?> = perfilRepository.getActivePerfil()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun selectPerfil(perfilId: Long) {
        viewModelScope.launch {
            perfilRepository.selectPerfil(perfilId)
        }
    }

    class Factory(private val repository: PerfilRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PersonaSelectionViewModel(repository) as T
        }
    }
}
