package com.nexusai.app.data.repository

import com.nexusai.app.data.local.dao.MensajeChatDao
import com.nexusai.app.data.local.entity.MensajeChatEntity
import com.nexusai.app.data.model.MensajeChat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepository(private val dao: MensajeChatDao) {

    fun getMensajesByPerfil(perfilId: Long): Flow<List<MensajeChat>> {
        return dao.getMensajesByPerfil(perfilId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getRecentMensajes(perfilId: Long, limit: Int = 20): List<MensajeChat> {
        return dao.getRecentMensajes(perfilId, limit).map { it.toDomain() }
    }

    suspend fun sendMessage(perfilId: Long, remitente: String, contenido: String): Long {
        return dao.insert(
            MensajeChatEntity(
                perfilId = perfilId,
                remitente = remitente,
                contenido = contenido
            )
        )
    }

    suspend fun deleteByPerfil(perfilId: Long) {
        dao.deleteByPerfil(perfilId)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    private fun MensajeChatEntity.toDomain() = MensajeChat(
        id = id,
        perfilId = perfilId,
        remitente = remitente,
        contenido = contenido,
        timestamp = timestamp
    )
}
