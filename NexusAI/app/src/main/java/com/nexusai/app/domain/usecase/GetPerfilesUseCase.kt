package com.nexusai.app.domain.usecase

import com.nexusai.app.data.model.PerfilIA
import com.nexusai.app.data.repository.PerfilRepository
import kotlinx.coroutines.flow.Flow

class GetPerfilesUseCase(private val repository: PerfilRepository) {

    operator fun invoke(): Flow<List<PerfilIA>> {
        return repository.getAllPerfiles()
    }

    fun getActive(): Flow<PerfilIA?> {
        return repository.getActivePerfil()
    }
}
