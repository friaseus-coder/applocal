package com.nexusai.app.ai.rag

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmbeddingEngine(private val context: Context) {

    companion object {
        private const val DIMENSION = 384
    }

    suspend fun generateEmbedding(text: String): FloatArray = withContext(Dispatchers.Default) {
        val normalized = text.lowercase().trim()
        val words = normalized.split("\\s+".toRegex())
        val vector = FloatArray(DIMENSION)

        for ((i, word) in words.withIndex()) {
            val hash = word.hashCode()
            val idx = (i * 31 + hash) % DIMENSION
            if (idx >= 0) {
                vector[idx] += 1.0f / words.size
            }
        }

        normalize(vector)
    }

    suspend fun generateEmbeddings(texts: List<String>): List<FloatArray> = withContext(Dispatchers.Default) {
        texts.map { generateEmbedding(it) }
    }

    private fun normalize(vector: FloatArray): FloatArray {
        val norm = kotlin.math.sqrt(vector.sumOf { (it * it).toDouble() }).toFloat()
        if (norm > 0f) {
            for (i in vector.indices) {
                vector[i] /= norm
            }
        }
        return vector
    }

    fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
        var dotProduct = 0f
        var normA = 0f
        var normB = 0f

        for (i in a.indices) {
            dotProduct += a[i] * b[i]
            normA += a[i] * a[i]
            normB += b[i] * b[i]
        }

        val denom = kotlin.math.sqrt((normA * normB).toDouble()).toFloat()
        return if (denom > 0f) dotProduct / denom else 0f
    }
}
