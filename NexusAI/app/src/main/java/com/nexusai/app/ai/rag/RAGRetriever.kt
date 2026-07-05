package com.nexusai.app.ai.rag

import android.content.Context
import android.util.Log
import com.nexusai.app.data.local.dao.MemoriaPerfilDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Fragmento de memoria recuperado con su puntuación de similitud semántica.
 */
data class FragmentoRAG(
    val texto: String,
    val similitud: Float = 0f
)

/**
 * Recuperador de contexto aumentado por generación (RAG).
 *
 * Realiza búsqueda vectorial semántica real sobre los fragmentos de memoria
 * almacenados en Room para un perfil de IA. Cada fragmento tiene un vector
 * de embedding (384 dims) serializado como string CSV.
 *
 * Flujo:
 * 1. Vectoriza la consulta del usuario con EmbeddingEngine (ONNX)
 * 2. Deserializa los vectores guardados en BD
 * 3. Calcula similitud coseno contra cada fragmento
 * 4. Filtra por umbral semántico (0.35) y devuelve los topK
 */
class RAGRetriever(
    private val context: Context,
    private val memoriaDao: MemoriaPerfilDao,
    private val embeddingEngine: EmbeddingEngine
) {

    companion object {
        private const val TAG = "NexusAI_RAG"

        /** Cantidad máxima de fragmentos a devolver */
        private const val TOP_K = 3

        /**
         * Umbral mínimo de similitud del coseno.
         * Por debajo de 0.35 la relación semántica se considera irrelevante.
         * Rango: 0.0 (ortogonal) a 1.0 (idéntico).
         */
        private const val UMBRAL_SIMILITUD = 0.35f
    }

    /**
     * Recupera los fragmentos de memoria más relevantes para una consulta.
     *
     * @param perfilId ID del perfil de IA dueño de las memorias
     * @param query    Texto de consulta del usuario
     * @param topK     Número máximo de fragmentos a devolver
     * @return Lista de fragmentos ordenados por similitud descendente
     */
    suspend fun retrieve(
        perfilId: Long,
        query: String,
        topK: Int = TOP_K
    ): List<FragmentoRAG> = withContext(Dispatchers.Default) {
        val memorias = memoriaDao.getMemoriasByPerfil(perfilId)
        if (memorias.isEmpty()) {
            Log.d(TAG, "No hay memorias indexadas para el perfil $perfilId")
            return@withContext emptyList()
        }

        Log.d(TAG, "Recuperando ${memorias.size} fragmentos de memoria del perfil $perfilId")

        // Vectorizar la consulta del usuario en tiempo real (384 dimensiones)
        val queryVector = embeddingEngine.generateEmbedding(query)
        Log.d(TAG, "Consulta vectorizada — 384 dimensiones")

        val fragmentosSimilares = mutableListOf<FragmentoRAG>()

        for (memoria in memorias) {
            // Deserializar el vector guardado en BD (formato: "0.123,0.456,...")
            val memVector = deserializarVector(memoria.vectorSerialized)

            val similitud = if (memVector != null) {
                // Ruta principal: usar vector precalculado de la BD
                embeddingEngine.cosineSimilarity(queryVector, memVector)
            } else {
                // Fallback: vectorizar el texto sobre la marcha
                Log.w(TAG, "Vector no encontrado en BD para fragmento #${memoria.id}, generando embedding on-the-fly")
                val vector = embeddingEngine.generateEmbedding(memoria.textoFragmento)
                embeddingEngine.cosineSimilarity(queryVector, vector)
            }

            // Aplicar umbral de relevancia semántica
            if (similitud >= UMBRAL_SIMILITUD) {
                fragmentosSimilares.add(FragmentoRAG(memoria.textoFragmento, similitud))
            }
        }

        // Ordenar por similitud descendente y tomar los topK
        val resultados = fragmentosSimilares
            .sortedByDescending { it.similitud }
            .take(topK)

        Log.d(TAG, "Recuperados ${resultados.size} fragmentos relevantes " +
                "(umbral > $UMBRAL_SIMILITUD) de ${memorias.size} totales")
        resultados
    }

    /**
     * Versión simplificada que devuelve el contexto formateado como string
     * listo para inyectar en el prompt del LLM.
     */
    suspend fun retrieveAsContext(perfilId: Long, query: String, topK: Int = TOP_K): String {
        val fragmentos = retrieve(perfilId, query, topK)
        if (fragmentos.isEmpty()) return ""

        return fragmentos.joinToString("\n\n") { fragmento ->
            "- \"${fragmento.texto}\" (similitud: ${"%.2f".format(fragmento.similitud)})"
        }
    }

    /**
     * Convierte un string CSV (ej: "0.123,0.456,0.789") de vuelta a FloatArray.
     * @return FloatArray con los valores, o null si el formato es inválido
     */
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
