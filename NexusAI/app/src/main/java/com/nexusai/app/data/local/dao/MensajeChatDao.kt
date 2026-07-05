package com.nexusai.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nexusai.app.data.local.entity.MensajeChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MensajeChatDao {

    @Query("SELECT * FROM historial_mensajes WHERE perfilId = :perfilId ORDER BY timestamp ASC")
    fun getMensajesByPerfil(perfilId: Long): Flow<List<MensajeChatEntity>>

    @Query("SELECT * FROM historial_mensajes WHERE perfilId = :perfilId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentMensajes(perfilId: Long, limit: Int = 20): List<MensajeChatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mensaje: MensajeChatEntity): Long

    @Query("DELETE FROM historial_mensajes WHERE perfilId = :perfilId")
    suspend fun deleteByPerfil(perfilId: Long)

    @Query("DELETE FROM historial_mensajes")
    suspend fun deleteAll()
}
