package com.nexusai.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nexusai.app.data.local.entity.MemoriaPerfilEntity

@Dao
interface MemoriaPerfilDao {

    @Query("SELECT * FROM memorias_perfil WHERE perfilId = :perfilId")
    suspend fun getMemoriasByPerfil(perfilId: Long): List<MemoriaPerfilEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(memorias: List<MemoriaPerfilEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(memoria: MemoriaPerfilEntity): Long

    @Query("DELETE FROM memorias_perfil WHERE perfilId = :perfilId")
    suspend fun deleteByPerfil(perfilId: Long)

    @Query("SELECT * FROM memorias_perfil WHERE perfilId = :perfilId ORDER BY id ASC LIMIT :limit")
    suspend fun getTopMemorias(perfilId: Long, limit: Int = 50): List<MemoriaPerfilEntity>
}
