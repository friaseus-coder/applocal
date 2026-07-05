package com.nexusai.app.domain.usecase

import com.nexusai.app.data.repository.PerfilRepository

class SelectPerfilUseCase(private val repository: PerfilRepository) {

    suspend operator fun invoke(perfilId: Long) {
        repository.selectPerfil(perfilId)
    }
}
