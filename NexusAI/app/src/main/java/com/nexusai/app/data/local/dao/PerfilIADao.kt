package com.nexusai.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nexusai.app.data.local.entity.PerfilIAEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PerfilIADao {

    @Query("SELECT * FROM perfiles_ia ORDER BY id ASC")
    fun getAllPerfiles(): Flow<List<PerfilIAEntity>>

    @Query("SELECT * FROM perfiles_ia WHERE isActive = 1 LIMIT 1")
    fun getActivePerfil(): Flow<PerfilIAEntity?>

    @Query("SELECT * FROM perfiles_ia WHERE id = :id")
    suspend fun getPerfilById(id: Long): PerfilIAEntity?

    @Query("UPDATE perfiles_ia SET isActive = 0 WHERE isActive = 1")
    suspend fun deactivateAll()

    @Query("UPDATE perfiles_ia SET isActive = 1 WHERE id = :id")
    suspend fun activatePerfil(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(perfiles: List<PerfilIAEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(perfil: PerfilIAEntity): Long

    @Update
    suspend fun update(perfil: PerfilIAEntity)

    @Delete
    suspend fun delete(perfil: PerfilIAEntity)
}
