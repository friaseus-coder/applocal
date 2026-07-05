package com.nexusai.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nexusai.app.data.local.entity.GustoUsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GustoUsuarioDao {

    @Query("SELECT * FROM gustos_usuario ORDER BY fechaDescubrimiento DESC")
    fun getAllGustos(): Flow<List<GustoUsuarioEntity>>

    @Query("SELECT * FROM gustos_usuario WHERE categoria = :categoria ORDER BY fechaDescubrimiento DESC")
    fun getGustosByCategoria(categoria: String): Flow<List<GustoUsuarioEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(gusto: GustoUsuarioEntity): Long

    @Delete
    suspend fun delete(gusto: GustoUsuarioEntity)

    @Query("DELETE FROM gustos_usuario")
    suspend fun deleteAll()

    @Query("SELECT * FROM gustos_usuario WHERE elemento LIKE '%' || :query || '%'")
    suspend fun searchGustos(query: String): List<GustoUsuarioEntity>
}
