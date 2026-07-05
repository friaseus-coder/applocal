package com.nexusai.app.domain.llm

import android.content.Context
import android.util.Log
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.nexusai.app.ai.security.ConstitutionalGuard
import com.nexusai.app.data.download.DownloadState
import com.nexusai.app.data.download.ModelDownloadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File

interface InferenceEngine {
    suspend fun loadModel(modelFileName: String): Result<Unit>
    suspend fun generateResponse(
        prompt: String,
        tipoPerfil: String,
        ragContext: String = "",
        webContext: String = "",
        callback: (String) -> Unit
    ): Result<String>
    suspend fun generateJsonExtraction(userMessage: String): Result<String>
    fun isReady(): Boolean
    fun unloadModel()
}

class LocalInferenceEngine(private val context: Context) : InferenceEngine {

    companion object {
        private const val TAG = "LocalInferenceEngine"
        private const val TEMPERATURA = 0.6f
        private const val MAX_TOKENS_SALIDA = 512
    }

    private var llmInference: LlmInference? = null
    private var isModelLoaded = false
    private val guard = ConstitutionalGuard()
    private val promptBuilder = PromptBuilder(context)

    override suspend fun loadModel(modelFileName: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Iniciando carga del modelo: $modelFileName")

                // Verificar que el archivo .bin exista en los datos locales
                val modelFile = File(context.filesDir, modelFileName)
                if (!modelFile.exists()) {
                    val msg = "Modelo no encontrado en: ${modelFile.absolutePath}"
                    Log.e(TAG, msg)
                    return@withContext Result.failure(Exception(msg))
                }

                Log.d(TAG, "Modelo encontrado en: ${modelFile.absolutePath}")

                // Cerrar sesión previa si existe
                llmInference?.close()
                llmInference = null

                // Construir opciones para MediaPipe LlmInference
                val options = LlmInference.LlmInferenceOptions.builder()
                    .setModelPath(modelFile.absolutePath)
                    .setMaxTokens(MAX_TOKENS_SALIDA)
                    .setTemperature(TEMPERATURA)
                    .build()

                llmInference = LlmInference.createFromOptions(context, options)
                isModelLoaded = true

                Log.d(TAG, "Modelo $modelFileName cargado exitosamente con MediaPipe")
                Result.success(Unit)
            } catch (e: Exception) {
                isModelLoaded = false
                Log.e(TAG, "Error al cargar el modelo: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    override suspend fun generateResponse(
        prompt: String,
        tipoPerfil: String,
        ragContext: String,
        webContext: String,
        callback: (String) -> Unit
    ): Result<String> = withContext(Dispatchers.Default) {
        try {
            // Construir el prompt completo: Directiva DUDH + personalidad + contexto
            // PromptBuilder resuelve el texto del perfil desde strings.xml según el tipo
            // y la Directiva DUDH desde R.string.dudh_directive para soporte multi-idioma
            val fullPrompt = promptBuilder.build(
                tipoPerfil = tipoPerfil,
                ragContext = ragContext,
                webContext = webContext,
                userMessage = prompt,
                historial = ""
            )

            // Escaneo de seguridad constitucional antes de inferir
            val safePrompt = guard.scanPrompt(fullPrompt)
            if (!safePrompt.isPermitted) {
                Log.w(TAG, "Prompt bloqueado por ConstitutionalGuard — devolviendo respuesta de seguridad")
                return@withContext Result.success(safePrompt.response)
            }

            Log.d(TAG, "Prompt consolidado de ${safePrompt.text.length} caracteres enviado a MediaPipe")
            val response = runInference(safePrompt.text)

            // Escaneo posterior de la respuesta generada
            val safeResponse = guard.scanResponse(response)
            Log.d(TAG, "Respuesta generada (${safeResponse.length} caracteres) verificada por ConstitutionalGuard")
            callback(safeResponse)
            Result.success(safeResponse)
        } catch (e: Exception) {
            Log.e(TAG, "Error en generateResponse: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun generateJsonExtraction(
        userMessage: String
    ): Result<String> = withContext(Dispatchers.Default) {
        try {
            val extractionPrompt = """
                A partir del siguiente mensaje del usuario, extrae información sobre sus gustos, intereses, disgustos o valores.
                Devuelve ÚNICA y EXCLUSIVAMENTE un objeto JSON plano sin explicaciones, sin markdown, sin bloques de código.
                No agregues etiquetas de código como '''json o '''. Solo el JSON.
                Formato obligatorio: {"categoria": "CATEGORIA", "elemento": "elemento_detectado", "sentimiento": "GUSTA/NO_GUSTA"}
                Si no hay información nueva, responde únicamente: {}
                Texto: "$userMessage"
            """.trimIndent()

            var response = runInference(extractionPrompt)

            // Limpiar posibles bloques markdown que el modelo pudiera añadir
            response = response
                .replace(Regex("""^```(?:json)?\s*"""), "")
                .replace(Regex("""\s*```$"""), "")
                .trim()

            // Validar que la respuesta sea JSON válido
            if (response.isNotBlank() && !response.startsWith("{") && !response.startsWith("{")) {
                Log.w(TAG, "Respuesta JSON inválida del extractor: $response")
                response = "{}"
            }

            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "Error en generateJsonExtraction: ${e.message}", e)
            Result.failure(e)
        }
    }

    private suspend fun runInference(prompt: String): String {
        val engine = llmInference
        if (engine == null) {
            val msg = "Error: El modelo de IA no está cargado. Debes invocar loadModel() antes de enviar mensajes."
            Log.w(TAG, msg)
            return msg
        }
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Iniciando inferencia MediaPipe — prompt de ${prompt.length} caracteres")
                val tiempoInicio = System.currentTimeMillis()
                val respuesta = engine.generateResponse(prompt)
                val tiempoTotal = System.currentTimeMillis() - tiempoInicio
                Log.d(TAG, "Inferencia MediaPipe completada en ${tiempoTotal}ms — ${respuesta.length} caracteres generados")
                respuesta
            } catch (e: Exception) {
                Log.e(TAG, "Error durante la inferencia MediaPipe: ${e.message}", e)
                "Error de inferencia: ${e.message}"
            }
        }
    }

    override fun isReady(): Boolean = isModelLoaded

    override fun unloadModel() {
        try {
            llmInference?.close()
            llmInference = null
            isModelLoaded = false
            Log.d(TAG, "Modelo descargado y recursos liberados correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error al liberar recursos del modelo: ${e.message}", e)
        }
    }

    fun descargarModelo(
        url: String,
        fileName: String = "gemma-2-2b-it-cpu-int4.bin"
    ) {
        val downloadManager = ModelDownloadManager(context)
        downloadManager.descargarModelo(url, fileName)
        Log.d(TAG, "Descarga de modelo iniciada desde $url")
    }

    fun observarDescargaModelo(): Flow<DownloadState> {
        val downloadManager = ModelDownloadManager(context)
        return downloadManager.observarEstado()
    }
}
