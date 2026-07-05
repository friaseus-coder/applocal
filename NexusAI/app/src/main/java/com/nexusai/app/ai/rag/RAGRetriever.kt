package com.nexusai.app.ai.rag

import android.content.Context
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

    suspend fun retrieve(
        perfilId: Long,
        query: String,
        topK: Int = 3
    ): List<FragmentoRAG> = withContext(Dispatchers.Default) {
        val memorias = memoriaDao.getMemoriasByPerfil(perfilId)
        if (memorias.isEmpty()) return@withContext emptyList()

        val queryVector = embeddingEngine.generateEmbedding(query)
        val fragmentosSimilares = mutableListOf<FragmentoRAG>()

        for (memoria in memorias) {
            val memVector = embeddingEngine.generateEmbedding(memoria.textoFragmento)
            val similarity = embeddingEngine.cosineSimilarity(queryVector, memVector)
            fragmentosSimilares.add(FragmentoRAG(memoria.textoFragmento, similarity))
        }

        fragmentosSimilares
            .sortedByDescending { it.similitud }
            .take(topK)
    }

    suspend fun retrieveAsContext(perfilId: Long, query: String, topK: Int = 3): String {
        val fragmentos = retrieve(perfilId, query, topK)
        if (fragmentos.isEmpty()) return ""

        return fragmentos.joinToString("\n\n") { fragmento ->
            "- \"${fragmento.texto}\""
        }
    }
}
