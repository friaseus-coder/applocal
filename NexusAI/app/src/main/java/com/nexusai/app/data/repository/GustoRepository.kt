package com.nexusai.app.data.repository

import com.nexusai.app.data.local.dao.GustoUsuarioDao
import com.nexusai.app.data.local.entity.GustoUsuarioEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class GustoUsuario(
    val id: Long,
    val categoria: String,
    val elemento: String,
    val sentimiento: String,
    val fechaDescubrimiento: Long
)

class GustoRepository(private val dao: GustoUsuarioDao) {

    fun getAllGustos(): Flow<List<GustoUsuario>> {
        return dao.getAllGustos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getGustosByCategoria(categoria: String): Flow<List<GustoUsuario>> {
        return dao.getGustosByCategoria(categoria).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insertGusto(
        categoria: String,
        elemento: String,
        sentimiento: String
    ): Long {
        return dao.insert(
            GustoUsuarioEntity(
                categoria = categoria,
                elemento = elemento,
                sentimiento = sentimiento
            )
        )
    }

    suspend fun deleteGusto(gusto: GustoUsuario) {
        dao.delete(
            GustoUsuarioEntity(
                id = gusto.id,
                categoria = gusto.categoria,
                elemento = gusto.elemento,
                sentimiento = gusto.sentimiento,
                fechaDescubrimiento = gusto.fechaDescubrimiento
            )
        )
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    private fun GustoUsuarioEntity.toDomain() = GustoUsuario(
        id = id,
        categoria = categoria,
        elemento = elemento,
        sentimiento = sentimiento,
        fechaDescubrimiento = fechaDescubrimiento
    )
}
