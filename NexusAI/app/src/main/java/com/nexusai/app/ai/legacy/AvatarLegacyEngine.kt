package com.nexusai.app.ai.legacy

import android.content.Context
import android.net.Uri
import com.nexusai.app.ai.rag.EmbeddingEngine
import com.nexusai.app.data.local.dao.MemoriaPerfilDao
import com.nexusai.app.data.local.entity.MemoriaPerfilEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class AvatarLegacyEngine(
    private val context: Context,
    private val memoriaDao: MemoriaPerfilDao,
    private val embeddingEngine: EmbeddingEngine
) {

    suspend fun procesarDocumento(
        uri: Uri,
        perfilId: Long,
        chunkSize: Int = 300
    ): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val text = readTextFromUri(uri)
            val chunks = chunkText(text, chunkSize)

            val embeddings = embeddingEngine.generateEmbeddings(chunks)

            val memorias = chunks.mapIndexed { index, chunk ->
                MemoriaPerfilEntity(
                    perfilId = perfilId,
                    textoFragmento = chunk,
                    vectorSerialized = embeddings[index].joinToString(",")
                )
            }

            memoriaDao.deleteByPerfil(perfilId)
            memoriaDao.insertAll(memorias)

            Result.success(chunks.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun readTextFromUri(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("No se pudo abrir el archivo")

        return BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
    }

    private fun chunkText(text: String, chunkSize: Int): List<String> {
        val chunks = mutableListOf<String>()
        val paragraphs = text.split("\n\n")

        val currentChunk = StringBuilder()
        for (paragraph in paragraphs) {
            if (currentChunk.length + paragraph.length > chunkSize && currentChunk.isNotEmpty()) {
                chunks.add(currentChunk.toString().trim())
                currentChunk.clear()
            }
            currentChunk.appendLine(paragraph)
        }

        if (currentChunk.isNotBlank()) {
            chunks.add(currentChunk.toString().trim())
        }

        return chunks
    }
}
