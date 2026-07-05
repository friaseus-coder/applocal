package com.nexusai.app.ai.rag

import android.content.Context
import android.util.Log
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.LongBuffer

/**
 * Motor de embeddings semánticos multilingüe basado en ONNX Runtime.
 *
 * Utiliza un modelo multilingüe (384 dimensiones) para convertir texto
 * en vectores densos semánticamente significativos en múltiples idiomas:
 * Castellano, Catalán, Inglés, Francés, Árabe e Hindi.
 *
 * La tokenización sigue el algoritmo WordPiece con vocabulario multilingüe
 * precargado desde assets. Soporta caracteres acentuados (es, fr, ca),
 * alfabeto árabe (RTL) y Devanagari (hindi) sin crashear.
 *
 * Todo el procesamiento matemático corre en Dispatchers.Default para
 * no bloquear el hilo principal de la UI.
 */
class EmbeddingEngine(private val context: Context) {

    companion object {
        private const val TAG = "NexusAI_ONNX"

        /** Nombre del modelo ONNX multilingüe en assets/ */
        private const val MODEL_NAME = "multilingual-embeddings.onnx"

        /** Nombre del vocabulario WordPiece multilingüe en assets/ */
        private const val VOCAB_NAME = "vocab-multilingual.txt"

        /** Dimensionalidad del vector de salida (384 para multilingual-e5-small / paraphrase-multilingual-MiniLM) */
        private const val DIMENSION = 384

        /** Longitud máxima de secuencia de tokens (padding/truncado) */
        private const val MAX_SEQ_LENGTH = 128

        // Tokens especiales del tokenizador WordPiece de BERT
        private const val TOKEN_CLS = "[CLS]"  // Inicio de secuencia (id 101)
        private const val TOKEN_SEP = "[SEP]"  // Fin de secuencia (id 102)
        private const val TOKEN_UNK = "[UNK]"  // Token desconocido (id 100)
        private const val ID_CLS = 101
        private const val ID_SEP = 102
        private const val ID_UNK = 100
    }

    // Recursos de ONNX Runtime
    private var ortSession: OrtSession? = null
    private var ortEnvironment: OrtEnvironment? = null

    /** Mapa token → id cargado desde assets/vocab.txt */
    private var vocab: Map<String, Int> = emptyMap()

    /** Indica si el modelo y vocabulario se cargaron correctamente */
    private var isLoaded = false

    init {
        // Carga sincrónica de recursos en el hilo de constructor
        // Los métodos públicos suspenden en Dispatchers.Default para no bloquear
        cargarRecursos()
    }

    // ------------------------------------------------------------------
    // Inicialización: modelo ONNX y vocabulario
    // ------------------------------------------------------------------

    /**
     * Carga el modelo multilingüe .onnx y el vocabulario desde
     * la carpeta assets/ del contexto de Android.
     * Los nombres de archivo se definen en MODEL_NAME y VOCAB_NAME.
     * Si ocurre algún error, isLoaded permanece en false y los métodos
     * de generación devuelven vectores vacíos sin crashear la app.
     */
    private fun cargarRecursos() {
        try {
            val inicio = System.currentTimeMillis()
            Log.d(TAG, "Iniciando carga de recursos ONNX multilingües...")

            // 1. Cargar vocabulario WordPiece multilingüe
            vocab = cargarVocabulario()
            Log.d(TAG, "Vocabulario multilingüe cargado: ${vocab.size} tokens desde $VOCAB_NAME")

            // 2. Inicializar el entorno ONNX Runtime (singleton global)
            ortEnvironment = OrtEnvironment.getEnvironment()

            // 3. Leer el modelo .onnx desde assets como arreglo de bytes
            val modeloBytes = context.assets.open(MODEL_NAME).use { it.readBytes() }
            Log.d(TAG, "Modelo ONNX multilingüe leído: ${modeloBytes.size} bytes desde $MODEL_NAME")

            // 4. Crear sesión de inferencia a partir de los bytes del modelo
            ortSession = ortEnvironment?.createSession(modeloBytes)

            isLoaded = true
            val elapsed = System.currentTimeMillis() - inicio
            Log.d(TAG, "Motor ONNX multilingüe inicializado correctamente en ${elapsed}ms")
        } catch (e: Exception) {
            Log.e(TAG, "ERROR crítico al cargar recursos ONNX: ${e.message}", e)
            isLoaded = false
        }
    }

    /**
     * Lee el archivo de vocabulario multilingüe desde assets.
     * Formato BERT WordPiece estándar: un token por línea, el índice
     * de la línea es el ID del token.
     */
    private fun cargarVocabulario(): Map<String, Int> {
        val mapa = mutableMapOf<String, Int>()
        val lector = context.assets.open(VOCAB_NAME).bufferedReader()
        lector.useLines { lineas ->
            lineas.forEachIndexed { indice, linea ->
                val token = linea.trim()
                if (token.isNotEmpty()) {
                    mapa[token] = indice
                }
            }
        }
        return mapa
    }

    // ------------------------------------------------------------------
    // Tokenización WordPiece multilingüe
    // ------------------------------------------------------------------

    /** Rango Unicode del alfabeto Árabe (U+0600 a U+06FF, extensiones) */
    private val ARABE_RANGO = Regex("""[\u0600-\u06FF\u0750-\u077F\u08A0-\u08FF]""")

    /** Rango Unicode del Devanagari (Hindi, U+0900 a U+097F) */
    private val DEVANAGARI_RANGO = Regex("""[\u0900-\u097F]""")

    /**
     * Convierte un texto de entrada en un arreglo de IDs de token
     * siguiendo el algoritmo WordPiece. Agrega automáticamente:
     *   - [CLS] (id 101) al inicio
     *   - [SEP] (id 102) al final
     *   - [UNK] (id 100) para palabras desconocidas
     *
     * Soporta caracteres acentuados (es, fr, ca), árabe (RTL) y
     * Devanagari (hindi). Trunca o rellena según MAX_SEQ_LENGTH.
     *
     * @param texto Texto en cualquier idioma soportado
     * @return Arreglo de IDs de token con [CLS] y [SEP]
     */
    private fun tokenizar(texto: String): IntArray {
        val tokens = mutableListOf<Int>()
        tokens.add(ID_CLS)

        // Paso 1: normalizar el texto
        // - convertir a minúsculas (respeta caracteres Unicode correctamente)
        // - separar por espacios y otros whitespace Unicode
        val palabras = texto.lowercase().trim().split(Regex("""[\s\u2000-\u200F\u2028-\u202F]+"""))

        for (palabraRaw in palabras) {
            if (tokens.size >= MAX_SEQ_LENGTH - 1) break
            if (palabraRaw.isBlank()) continue

            // Paso 2: limpiar puntuación conservando letras multilingües
            val palabraLimpia = limpiarPuntuacion(palabraRaw)
            if (palabraLimpia.isBlank()) continue

            // Paso 3: tokenizar la palabra limpia
            val subTokens = wordPieceTokenizar(palabraLimpia)
            for (sub in subTokens) {
                if (tokens.size >= MAX_SEQ_LENGTH - 1) break
                tokens.add(sub)
            }
        }

        tokens.add(ID_SEP)
        return tokens.toIntArray()
    }

    /**
     * Limpia la puntuación de una palabra conservando únicamente
     * letras, números y caracteres específicos de cada idioma.
     *
     * Para alfabeto árabe y devanagari se conservan TODOS los caracteres
     * del rango Unicode (no se aplica el filtro de puntuación latina).
     */
    private fun limpiarPuntuacion(palabra: String): String {
        // Detectar si es árabe o devanagari — no filtrar caracteres
        if (ARABE_RANGO.containsMatchIn(palabra) || DEVANAGARI_RANGO.containsMatchIn(palabra)) {
            return palabra
        }
        // Para alfabetos latinos: conservar solo letras, números, apóstrofes
        return palabra.filter { it in "abcdefghijklmnopqrstuvwxyzàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþ0123456789'" }
    }

    /**
     * Tokenización WordPiece de una palabra individual:
     * 1. Busca la palabra completa en el vocabulario multilingüe.
     * 2. Si no existe, prueba subcadenas decrecientes con prefijo "##".
     * 3. Si no hay coincidencia parcial, asigna [UNK] (ID 100).
     *
     * Nunca lanza excepción: cualquier token no encontrado se resuelve
     * al token desconocido, manteniendo la corrutina en ejecución.
     */
    private fun wordPieceTokenizar(palabra: String): List<Int> {
        val ids = mutableListOf<Int>()

        if (palabra.isEmpty()) return ids

        // Caso 1: coincidencia exacta de la palabra completa en el vocabulario
        vocab[palabra]?.let {
            ids.add(it)
            return ids
        }

        // Caso 2: descomposición greedy en sub-palabras con prefijo "##"
        var restante = palabra
        while (restante.isNotEmpty()) {
            var subLen = restante.length
            var encontrado = false
            while (subLen > 0) {
                val prefijo = if (restante.length == palabra.length) "" else "##"
                val candidato = "$prefijo${restante.substring(0, subLen)}"
                val id = vocab[candidato]
                if (id != null) {
                    ids.add(id)
                    restante = restante.substring(subLen)
                    encontrado = true
                    break
                }
                subLen--
            }
            // Caso 3: no se encontró ninguna subcadena → token desconocido
            // Esto puede ocurrir con caracteres raros, emojis, o palabras
            // en alfabetos no cubiertos por el vocabulario. No lanzar excepción.
            if (!encontrado) {
                ids.add(ID_UNK)
                break
            }
        }
        return ids
    }

    // ------------------------------------------------------------------
    // Generación de embeddings
    // ------------------------------------------------------------------

    /**
     * Genera el vector de embedding semántico para un texto dado.
     *
     * Flujo interno:
     * 1. Tokenización WordPiece → IDs numéricos.
     * 2. Preparación de tensores: input_ids, attention_mask, token_type_ids.
     * 3. Inferencia con ONNX Runtime → tensor de salida con 384 dims por token.
     * 4. CLS Pooling: extrae la representación del primer token ([CLS]).
     * 5. Normalización L2 del vector resultante.
     *
     * @param text Texto de entrada a vectorizar.
     * @return FloatArray de 384 dimensiones normalizado (L2). Vacío si hay error.
     */
    suspend fun generateEmbedding(text: String): FloatArray = withContext(Dispatchers.Default) {
        if (!isLoaded) {
            Log.w(TAG, "Modelo ONNX no disponible — devolviendo vector vacío")
            return@withContext FloatArray(DIMENSION)
        }

        try {
            val sesion = ortSession ?: return@withContext FloatArray(DIMENSION)
            val entorno = ortEnvironment ?: return@withContext FloatArray(DIMENSION)

            // --- 1. Tokenizar y preparar buffers ---
            val inputIds = tokenizar(text)
            val longSecuencia = inputIds.size.coerceAtMost(MAX_SEQ_LENGTH)

            // Tensores de entrada con padding a MAX_SEQ_LENGTH
            val paddedInputIds = LongArray(MAX_SEQ_LENGTH) { 0L }  // IDs de token
            val attentionMask   = LongArray(MAX_SEQ_LENGTH) { 0L } // 1 = token real, 0 = padding
            val tokenTypeIds    = LongArray(MAX_SEQ_LENGTH) { 0L } // segmento A (siempre 0)

            for (i in 0 until longSecuencia) {
                paddedInputIds[i] = inputIds[i].toLong()
                attentionMask[i] = 1L
            }

            // Shape: [batch_size=1, max_seq_length=128]
            val shape = longArrayOf(1L, MAX_SEQ_LENGTH.toLong())

            // --- 2. Crear tensores ONNX desde los buffers ---
            val tensorInputIds = OnnxTensor.createTensor(
                entorno, LongBuffer.wrap(paddedInputIds), shape
            )
            val tensorAttentionMask = OnnxTensor.createTensor(
                entorno, LongBuffer.wrap(attentionMask), shape
            )
            val tensorTokenTypeIds = OnnxTensor.createTensor(
                entorno, LongBuffer.wrap(tokenTypeIds), shape
            )

            // Mapa de nombres de entrada del modelo (convención BERT)
            val entradas = mapOf(
                "input_ids" to tensorInputIds,
                "attention_mask" to tensorAttentionMask,
                "token_type_ids" to tensorTokenTypeIds
            )

            // --- 3. Ejecutar inferencia ---
            val resultados = sesion.run(entradas)

            // El tensor de salida tiene forma [1, 128, 384]
            // Tomamos la primera fila (índice 0) = representación del token [CLS]
            val tensorSalida = resultados.get(0) as OnnxTensor
            val bufferSalida = tensorSalida.floatBuffer

            // --- 4. CLS Pooling: extraer las primeras 384 dimensiones ---
            val embedding = FloatArray(DIMENSION)
            for (i in 0 until DIMENSION) {
                embedding[i] = bufferSalida.get(i)
            }

            // Cerrar recursos para evitar fugas de memoria nativa
            resultados.close()
            tensorInputIds.close()
            tensorAttentionMask.close()
            tensorTokenTypeIds.close()

            // --- 5. Normalización L2 ---
            normalizarL2(embedding)

        } catch (e: Exception) {
            Log.e(TAG, "Error en generación de embedding: ${e.message}", e)
            FloatArray(DIMENSION) // vector vacío seguro
        }
    }

    /**
     * Genera embeddings para múltiples textos en paralelo.
     * Útil durante la indexación de documentos (chunks de PDF/texto).
     */
    suspend fun generateEmbeddings(texts: List<String>): List<FloatArray> = withContext(Dispatchers.Default) {
        texts.map { generateEmbedding(it) }
    }

    // ------------------------------------------------------------------
    // Normalización y similitud
    // ------------------------------------------------------------------

    /**
     * Normaliza un vector por su norma L2 (euclidiana).
     * Si la norma es 0 (vector vacío), lo deja intacto.
     */
    private fun normalizarL2(vector: FloatArray): FloatArray {
        val norma = kotlin.math.sqrt(vector.sumOf { (it * it).toDouble() }).toFloat()
        if (norma > 0f) {
            for (i in vector.indices) {
                vector[i] /= norma
            }
        }
        return vector
    }

    /**
     * Calcula la similitud del coseno entre dos vectores de igual dimensión.
     * 
     * Fórmula: cos(θ) = (A · B) / (||A|| * ||B||)
     * Resultado: 1.0 = idénticos, 0.0 = ortogonales, -1.0 = opuestos.
     * Como los vectores ya están normalizados (L2), la norma siempre debe ser 1.
     */
    fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
        var productoPunto = 0f
        var normaA = 0f
        var normaB = 0f

        for (i in a.indices) {
            productoPunto += a[i] * b[i]
            normaA += a[i] * a[i]
            normaB += b[i] * b[i]
        }

        val denominador = kotlin.math.sqrt((normaA * normaB).toDouble()).toFloat()
        return if (denominador > 0f) productoPunto / denominador else 0f
    }

    // ------------------------------------------------------------------
    // Liberación de recursos
    // ------------------------------------------------------------------

    /**
     * Libera la sesión y el entorno de ONNX Runtime.
     * Debe llamarse cuando el engine ya no se necesite (ej. onDestroy).
     */
    fun close() {
        try {
            ortSession?.close()
            ortSession = null
            ortEnvironment?.close()
            ortEnvironment = null
            isLoaded = false
            Log.d(TAG, "Recursos ONNX liberados correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error al liberar recursos ONNX: ${e.message}", e)
        }
    }
}
