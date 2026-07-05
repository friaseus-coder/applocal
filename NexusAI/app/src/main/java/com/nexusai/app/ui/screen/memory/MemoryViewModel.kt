package com.nexusai.app.ui.screen.memory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nexusai.app.data.repository.GustoRepository
import com.nexusai.app.data.repository.GustoUsuario
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MemoryViewModel(
    private val gustoRepository: GustoRepository
) : ViewModel() {

    val gustos: StateFlow<List<GustoUsuario>> = gustoRepository.getAllGustos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun deleteGusto(gusto: GustoUsuario) {
        viewModelScope.launch {
            gustoRepository.deleteGusto(gusto)
        }
    }

    fun clearAllMemory() {
        viewModelScope.launch {
            gustoRepository.deleteAll()
        }
    }

    class Factory(private val repository: GustoRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MemoryViewModel(repository) as T
        }
    }
}
