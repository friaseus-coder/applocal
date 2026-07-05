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
        private const val TAG = "NexusAI_DebtMitigation"
        private const val TOP_K = 3
        private const val UMBRAL_SIMILITUD = 0.35f
    }

    private val cacheEmbeddings = mutableMapOf<Long, List<Pair<String, FloatArray>>>()

    suspend fun cargarPerfilEnCache(perfilId: Long) = withContext(Dispatchers.IO) {
        Log.d(TAG, "Cargando embeddings en RAM para perfil $perfilId")
        val memorias = memoriaDao.getMemoriasByPerfil(perfilId)
        val embeddings = memorias.mapNotNull { memoria ->
            val vector = deserializarVector(memoria.vectorSerialized)
            if (vector != null) Pair(memoria.textoFragmento, vector) else null
        }
        cacheEmbeddings[perfilId] = embeddings
        Log.d(TAG, "Cache RAM poblada con ${embeddings.size} embeddings para perfil $perfilId (${memorias.size} fragmentos totales)")
    }

    suspend fun retrieve(
        perfilId: Long,
        query: String,
        topK: Int = TOP_K
    ): List<FragmentoRAG> = withContext(Dispatchers.Default) {
        val embeddings = cacheEmbeddings[perfilId]
        if (embeddings.isNullOrEmpty()) {
            Log.d(TAG, "Cache de embeddings vacia para perfil $perfilId — sin memorias que recuperar")
            return@withContext emptyList()
        }

        Log.d(TAG, "Recuperando entre ${embeddings.size} embeddings en RAM para perfil $perfilId")
        val queryVector = embeddingEngine.generateEmbedding(query)
        Log.d(TAG, "Consulta vectorizada — 384 dimensiones")

        val fragmentosSimilares = embeddings.mapNotNull { (texto, vector) ->
            val similitud = embeddingEngine.cosineSimilarity(queryVector, vector)
            if (similitud >= UMBRAL_SIMILITUD) FragmentoRAG(texto, similitud) else null
        }

        val resultados = fragmentosSimilares
            .sortedByDescending { it.similitud }
            .take(topK)

        Log.d(TAG, "Recuperados ${resultados.size} fragmentos relevantes (umbral > $UMBRAL_SIMILITUD) de ${embeddings.size} en cache RAM")
        resultados
    }

    suspend fun retrieveAsContext(perfilId: Long, query: String, topK: Int = TOP_K): String {
        val fragmentos = retrieve(perfilId, query, topK)
        if (fragmentos.isEmpty()) return ""
        return fragmentos.joinToString("\n\n") { fragmento ->
            "- \"${fragmento.texto}\" (similitud: ${"%.2f".format(fragmento.similitud)})"
        }
    }

    fun limpiarCache(perfilId: Long) {
        cacheEmbeddings.remove(perfilId)
        Log.d(TAG, "Cache RAM liberada para perfil $perfilId")
    }

    private fun deserializarVector(serializado: String?): FloatArray? {
        if (serializado.isNullOrBlank()) return null
        return try {
            val partes = serializado.split(",")
            FloatArray(partes.size) { partes[it].toFloat() }
        } catch (e: Exception) {
            Log.e(TAG, "Error al deserializar vector desde BD: ${e.message}")
            null
        }
    }
}
