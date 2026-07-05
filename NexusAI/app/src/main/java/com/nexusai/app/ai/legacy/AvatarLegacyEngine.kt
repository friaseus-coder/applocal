package com.nexusai.app.ai.legacy

import android.content.Context
import android.net.Uri
import android.util.Log
import com.nexusai.app.ai.rag.EmbeddingEngine
import com.nexusai.app.data.local.dao.MemoriaPerfilDao
import com.nexusai.app.data.local.entity.MemoriaPerfilEntity
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class AvatarLegacyEngine(
    private val context: Context,
    private val memoriaDao: MemoriaPerfilDao,
    private val embeddingEngine: EmbeddingEngine
) {

    companion object {
        private const val TAG = "AvatarLegacyEngine"
        private const val TAMANO_CHUNK = 300
    }

    init {
        PDFBoxResourceLoader.init(context)
        Log.d(TAG, "PDFBoxResourceLoader inicializado correctamente")
    }

    suspend fun procesarDocumento(
        uri: Uri,
        perfilId: Long,
        chunkSize: Int = TAMANO_CHUNK
    ): Result<Int> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Procesando documento Uri: $uri")

            val tipoMime = context.contentResolver.getType(uri) ?: ""
            val nombreArchivo = obtenerNombreArchivo(uri)
            val extension = nombreArchivo.substringAfterLast('.', "").lowercase()

            Log.d(TAG, "Tipo MIME: $tipoMime, Extensión: $extension, Archivo: $nombreArchivo")

            val text = if (extension == "pdf" || tipoMime.contains("pdf")) {
                extraerTextoPdf(uri)
            } else if (extension == "txt" || tipoMime.contains("text")) {
                extraerTextoPlano(uri)
            } else {
                val msg = "Formato de archivo no soportado: $extension ($tipoMime)"
                Log.e(TAG, msg)
                return@withContext Result.failure(Exception(msg))
            }

            if (text.isBlank()) {
                val msg = "El documento no contiene texto extraíble"
                Log.w(TAG, msg)
                return@withContext Result.failure(Exception(msg))
            }

            Log.d(TAG, "Texto extraído: ${text.length} caracteres")

            val chunks = chunkText(text, chunkSize)
            Log.d(TAG, "Texto segmentado en ${chunks.size} fragmentos")

            val embeddings = embeddingEngine.generateEmbeddings(chunks)
            Log.d(TAG, "Embeddings generados para ${embeddings.size} fragmentos")

            val memorias = chunks.mapIndexed { index, chunk ->
                MemoriaPerfilEntity(
                    perfilId = perfilId,
                    textoFragmento = chunk,
                    vectorSerialized = embeddings[index].joinToString(",")
                )
            }

            memoriaDao.deleteByPerfil(perfilId)
            memoriaDao.insertAll(memorias)

            Log.d(TAG, "Guardados ${memorias.size} fragmentos en la base de datos")
            Result.success(chunks.size)
        } catch (e: Exception) {
            Log.e(TAG, "Error al procesar documento: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun extraerTextoPdf(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("No se pudo abrir el archivo PDF")

        return inputStream.use { stream ->
            PDDocument.load(stream).use { documento ->
                val stripper = PDFTextStripper()
                stripper.sortByPosition = true
                stripper.getText(documento)
            }
        }
    }

    private fun extraerTextoPlano(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("No se pudo abrir el archivo de texto")

        return BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
    }

    private fun obtenerNombreArchivo(uri: Uri): String {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            if (it.moveToFirst()) {
                val idx = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (idx >= 0) it.getString(idx) else uri.lastPathSegment ?: "desconocido"
            } else {
                uri.lastPathSegment ?: "desconocido"
            }
        } ?: uri.lastPathSegment ?: "desconocido"
    }

    private fun chunkText(text: String, chunkSize: Int): List<String> {
        val limpio = text
            .replace(Regex("""\r\n"""), "\n")
            .replace(Regex("""[ \t]+"""), " ")
            .replace(Regex("""\n{3,}"""), "\n\n")
            .trim()

        val chunks = mutableListOf<String>()
        val parrafos = limpio.split(Regex("""\n\n"""))
        val currentChunk = StringBuilder()

        fun cerrarFragmento() {
            val fragmento = currentChunk.toString().trim()
            if (fragmento.isNotBlank()) {
                chunks.add(fragmento)
            }
            currentChunk.clear()
        }

        for (parrafo in parrafos) {
            val limpioParrafo = parrafo.trim()
            if (limpioParrafo.isBlank()) continue

            if (currentChunk.isNotEmpty() && currentChunk.length + limpioParrafo.length + 1 > chunkSize) {
                cerrarFragmento()
            }

            if (limpioParrafo.length > chunkSize) {
                // Dividir párrafos muy largos por oraciones
                val oraciones = limpioParrafo.split(Regex("""(?<=[.!?])\s+"""))
                for (oracion in oraciones) {
                    if (currentChunk.isNotEmpty() && currentChunk.length + oracion.length + 1 > chunkSize) {
                        cerrarFragmento()
                    }
                    if (currentChunk.isNotEmpty()) currentChunk.append(" ")
                    currentChunk.append(oracion)
                }
            } else {
                if (currentChunk.isNotEmpty()) currentChunk.append("\n\n")
                currentChunk.append(limpioParrafo)
            }
        }

        cerrarFragmento()
        return chunks
    }
}
