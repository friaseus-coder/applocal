package com.nexusai.app.data.repository

import com.nexusai.app.data.local.dao.MemoriaPerfilDao
import com.nexusai.app.data.local.entity.MemoriaPerfilEntity

class MemoriaRepository(private val dao: MemoriaPerfilDao) {

    suspend fun getMemoriasByPerfil(perfilId: Long): List<String> {
        return dao.getMemoriasByPerfil(perfilId).map { it.textoFragmento }
    }

    suspend fun insertMemoria(perfilId: Long, texto: String, vector: String? = null): Long {
        return dao.insert(
            MemoriaPerfilEntity(
                perfilId = perfilId,
                textoFragmento = texto,
                vectorSerialized = vector
            )
        )
    }

    suspend fun insertAll(memorias: List<MemoriaPerfilEntity>) {
        dao.insertAll(memorias)
    }

    suspend fun deleteByPerfil(perfilId: Long) {
        dao.deleteByPerfil(perfilId)
    }

    suspend fun getTopMemorias(perfilId: Long, limit: Int = 50): List<String> {
        return dao.getTopMemorias(perfilId, limit).map { it.textoFragmento }
    }
}
