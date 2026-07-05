package com.nexusai.app.ai.rag

import android.content.Context
import android.util.Log
import com.nexusai.app.data.local.dao.MemoriaPerfilDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class FragmentoRAG(
    val texto: String,
    val similitud: Float = 0f
)

class RAGRetriever(
    private val context: Context,
    private val memoriaDao: MemoriaPerfilDao,
    private val embeddingEngine: EmbeddingEngine
) {

    companion object {
        private const val TAG = "RAGRetriever"
        private const val TOP_K = 3
        private const val UMBRAL_SIMILITUD = 0.35f
    }

    suspend fun retrieve(
        perfilId: Long,
        query: String,
        topK: Int = TOP_K
    ): List<FragmentoRAG> = withContext(Dispatchers.Default) {
        val memorias = memoriaDao.getMemoriasByPerfil(perfilId)
        if (memorias.isEmpty()) {
            Log.d(TAG, "No hay memorias para el perfil $perfilId")
            return@withContext emptyList()
        }

        Log.d(TAG, "Recuperando ${memorias.size} fragmentos de memoria para el perfil $perfilId")

        val queryVector = embeddingEngine.generateEmbedding(query)
        val fragmentosSimilares = mutableListOf<FragmentoRAG>()

        for (memoria in memorias) {
            // Deserializar el vector guardado en la base de datos
            val memVector = deserializarVector(memoria.vectorSerialized)
            if (memVector == null) {
                // Fallback: generar embedding del texto si no hay vector serializado
                Log.w(TAG, "Vector no encontrado en BD, generando embedding del texto")
                val vector = embeddingEngine.generateEmbedding(memoria.textoFragmento)
                val similitud = embeddingEngine.cosineSimilarity(queryVector, vector)
                if (similitud >= UMBRAL_SIMILITUD) {
                    fragmentosSimilares.add(FragmentoRAG(memoria.textoFragmento, similitud))
                }
            } else {
                val similitud = embeddingEngine.cosineSimilarity(queryVector, memVector)
                if (similitud >= UMBRAL_SIMILITUD) {
                    fragmentosSimilares.add(FragmentoRAG(memoria.textoFragmento, similitud))
                }
            }
        }

        val resultados = fragmentosSimilares
            .sortedByDescending { it.similitud }
            .take(topK)

        Log.d(TAG, "Recuperados ${resultados.size} fragmentos relevantes " +
                "(umbral > $UMBRAL_SIMILITUD) de ${memorias.size} totales")
        resultados
    }

    suspend fun retrieveAsContext(perfilId: Long, query: String, topK: Int = TOP_K): String {
        val fragmentos = retrieve(perfilId, query, topK)
        if (fragmentos.isEmpty()) return ""

        return fragmentos.joinToString("\n\n") { fragmento ->
            "- \"${fragmento.texto}\" (similitud: ${"%.2f".format(fragmento.similitud)})"
        }
    }

    private fun deserializarVector(serializado: String?): FloatArray? {
        if (serializado.isNullOrBlank()) return null
        return try {
            val partes = serializado.split(",")
            FloatArray(partes.size) { partes[it].toFloat() }
        } catch (e: Exception) {
            Log.e(TAG, "Error al deserializar vector: ${e.message}")
            null
        }
    }
}
