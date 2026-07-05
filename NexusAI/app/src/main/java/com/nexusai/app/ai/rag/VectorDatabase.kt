package com.nexusai.app.ai.rag

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class VectorRecord(
    val id: Long,
    val perfilId: Long,
    val text: String,
    val vector: FloatArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VectorRecord) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

class VectorDatabase {

    private val records = mutableListOf<VectorRecord>()
    private val mutex = Mutex()

    suspend fun insert(record: VectorRecord) = mutex.withLock {
        records.add(record)
    }

    suspend fun insertAll(records: List<VectorRecord>) = mutex.withLock {
        this.records.addAll(records)
    }

    suspend fun search(queryVector: FloatArray, perfilId: Long, topK: Int = 3): List<VectorRecord> {
        return mutex.withLock {
            val scored = records
                .filter { it.perfilId == perfilId }
                .map { it to cosineSimilarity(queryVector, it.vector) }
                .sortedByDescending { (_, score) -> score }
                .take(topK)

            scored.map { (record, _) -> record }
        }
    }

    suspend fun deleteByPerfil(perfilId: Long) = mutex.withLock {
        records.removeAll { it.perfilId == perfilId }
    }

    suspend fun clear() = mutex.withLock {
        records.clear()
    }

    suspend fun count(): Int = mutex.withLock { records.size }

    private fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
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
