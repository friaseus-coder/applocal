package com.nexusai.app.data.repository

import com.nexusai.app.data.local.dao.PerfilIADao
import com.nexusai.app.data.local.entity.PerfilIAEntity
import com.nexusai.app.data.model.PerfilIA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PerfilRepository(private val dao: PerfilIADao) {

    fun getAllPerfiles(): Flow<List<PerfilIA>> {
        return dao.getAllPerfiles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getActivePerfil(): Flow<PerfilIA?> {
        return dao.getActivePerfil().map { it?.toDomain() }
    }

    suspend fun getPerfilById(id: Long): PerfilIA? {
        return dao.getPerfilById(id)?.toDomain()
    }

    suspend fun selectPerfil(id: Long) {
        dao.deactivateAll()
        dao.activatePerfil(id)
    }

    suspend fun seedPerfiles(perfiles: List<PerfilIAEntity>) {
        dao.insertAll(perfiles)
    }

    private fun PerfilIAEntity.toDomain() = PerfilIA(
        id = id,
        nombre = nombre,
        tipo = tipo,
        descripcion = descripcion,
        icono = icono,
        promptSistemaBase = promptSistemaBase,
        tagsRag = tagsRag,
        isActive = isActive
    )
}
