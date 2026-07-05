package com.nexusai.app.ai.rag

import android.content.Context
import android.util.Log
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.LongBuffer
import java.util.Scanner

class EmbeddingEngine(private val context: Context) {

    companion object {
        private const val TAG = "EmbeddingEngine"
        private const val DIMENSION = 384
        private const val MAX_SEQ_LENGTH = 128
        private const val TOKEN_CLS = "[CLS]"
        private const val TOKEN_SEP = "[SEP]"
        private const val TOKEN_UNK = "[UNK]"
        private const val ID_CLS = 101
        private const val ID_SEP = 102
        private const val ID_UNK = 100
    }

    private var ortSession: OrtSession? = null
    private var ortEnvironment: OrtEnvironment? = null
    private var vocab: Map<String, Int> = emptyMap()
    private var isLoaded = false

    init {
        // Cargar modelo y vocabulario desde assets al crear la instancia
        loadResources()
    }

    private fun loadResources() {
        try {
            val startTime = System.currentTimeMillis()

            // Cargar vocabulario WordPiece desde assets/vocab.txt
            vocab = loadVocabulary()
            Log.d(TAG, "Vocabulario cargado: ${vocab.size} tokens")

            // Inicializar ONNX Runtime y cargar el modelo
            ortEnvironment = OrtEnvironment.getEnvironment()
            val modelBytes = context.assets.open("all-MiniLM-L6-v2.onnx").use { it.readBytes() }
            ortSession = ortEnvironment?.createSession(modelBytes)
            Log.d(TAG, "Modelo ONNX cargado correctamente")

            isLoaded = true
            val elapsed = System.currentTimeMillis() - startTime
            Log.d(TAG, "EmbeddingEngine inicializado en ${elapsed}ms")
        } catch (e: Exception) {
            Log.e(TAG, "Error al cargar recursos del embedding: ${e.message}", e)
            isLoaded = false
        }
    }

    private fun loadVocabulary(): Map<String, Int> {
        val vocabMap = mutableMapOf<String, Int>()
        val reader = context.assets.open("vocab.txt").bufferedReader()
        reader.useLines { lines ->
            lines.forEachIndexed { index, line ->
                val token = line.trim()
                if (token.isNotEmpty()) {
                    vocabMap[token] = index
                }
            }
        }
        return vocabMap
    }

    /**
     * Tokenización WordPiece básica.
     * Divide el texto en palabras, luego sub-word tokens según el vocabulario,
     * y asigna IDs con los tokens especiales [CLS] y [SEP].
     */
    private fun tokenize(text: String): IntArray {
        val tokens = mutableListOf<Int>()
        tokens.add(ID_CLS)

        val palabras = text.lowercase().trim().split(Regex("\\s+"))
        for (palabra in palabras) {
            if (tokens.size >= MAX_SEQ_LENGTH - 1) break
            val subTokens = wordPieceTokenize(palabra)
            for (sub in subTokens) {
                if (tokens.size >= MAX_SEQ_LENGTH - 1) break
                tokens.add(sub)
            }
        }

        tokens.add(ID_SEP)
        return tokens.toIntArray()
    }

    /**
     * WordPiece: busca la palabra completa en el vocabulario.
     * Si no existe, la descompone en sub-palabras con prefijo "##".
     * Si no hay coincidencia, usa [UNK].
     */
    private fun wordPieceTokenize(palabra: String): List<Int> {
        val ids = mutableListOf<Int>()

        // Intentar coincidencia exacta
        vocab[palabra]?.let {
            ids.add(it)
            return ids
        }

        // Descomposición en sub-palabras
        var remaining = palabra
        while (remaining.isNotEmpty()) {
            var subLen = remaining.length
            var found = false
            while (subLen > 0) {
                val prefix = if (remaining.length == palabra.length) "" else "##"
                val candidate = "$prefix${remaining.substring(0, subLen)}"
                val id = vocab[candidate]
                if (id != null) {
                    ids.add(id)
                    remaining = remaining.substring(subLen)
                    found = true
                    break
                }
                subLen--
            }
            if (!found) {
                ids.add(ID_UNK)
                break
            }
        }
        return ids
    }

    suspend fun generateEmbedding(text: String): FloatArray = withContext(Dispatchers.Default) {
        if (!isLoaded) {
            Log.w(TAG, "Modelo no cargado, devolviendo vector vacío")
            return@withContext FloatArray(DIMENSION)
        }

        try {
            val session = ortSession ?: return@withContext FloatArray(DIMENSION)
            val env = ortEnvironment ?: return@withContext FloatArray(DIMENSION)

            val inputIds = tokenize(text)
            val seqLength = inputIds.size.coerceAtMost(MAX_SEQ_LENGTH)

            val paddedInputIds = LongArray(MAX_SEQ_LENGTH) { 0L }
            val attentionMask = LongArray(MAX_SEQ_LENGTH) { 0L }
            val tokenTypeIds = LongArray(MAX_SEQ_LENGTH) { 0L }

            for (i in 0 until seqLength) {
                paddedInputIds[i] = inputIds[i].toLong()
                attentionMask[i] = 1L
            }

            val shape = longArrayOf(1L, MAX_SEQ_LENGTH.toLong())

            val inputIdsTensor = OnnxTensor.createTensor(
                env, LongBuffer.wrap(paddedInputIds), shape
            )
            val attentionMaskTensor = OnnxTensor.createTensor(
                env, LongBuffer.wrap(attentionMask), shape
            )
            val tokenTypeIdsTensor = OnnxTensor.createTensor(
                env, LongBuffer.wrap(tokenTypeIds), shape
            )

            val inputs = mapOf(
                "input_ids" to inputIdsTensor,
                "attention_mask" to attentionMaskTensor,
                "token_type_ids" to tokenTypeIdsTensor
            )

            val results = session.run(inputs)

            val outputTensor = results.get(0) as OnnxTensor
            val outputData = outputTensor.floatBuffer

            // CLS Pooling: tomar el vector del primer token (CLS)
            val embedding = FloatArray(DIMENSION)
            for (i in 0 until DIMENSION) {
                embedding[i] = outputData.get(i)
            }

            // Cerrar recursos
            results.close()
            inputIdsTensor.close()
            attentionMaskTensor.close()
            tokenTypeIdsTensor.close()

            // Normalización L2
            normalize(embedding)
        } catch (e: Exception) {
            Log.e(TAG, "Error al generar embedding: ${e.message}", e)
            FloatArray(DIMENSION)
        }
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

    fun close() {
        try {
            ortSession?.close()
            ortEnvironment?.close()
            Log.d(TAG, "Recursos de ONNX liberados")
        } catch (e: Exception) {
            Log.e(TAG, "Error al cerrar recursos: ${e.message}", e)
        }
    }
}
