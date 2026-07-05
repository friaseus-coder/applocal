package com.nexusai.app.domain.llm

import android.content.Context
import android.util.Log
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.nexusai.app.ai.security.ConstitutionalGuard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

interface InferenceEngine {
    suspend fun loadModel(modelFileName: String): Result<Unit>
    suspend fun generateResponse(
        prompt: String,
        perfilPrompt: String,
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
        perfilPrompt: String,
        ragContext: String,
        webContext: String,
        callback: (String) -> Unit
    ): Result<String> = withContext(Dispatchers.Default) {
        try {
            // Inyectar la Directiva Suprema de Seguridad (DUDH) como capa base del sistema
            val promptSeguro = guard.buildSystemPrompt(perfilPrompt)

            val fullPrompt = PromptBuilder.build(
                perfilPrompt = promptSeguro,
                ragContext = ragContext,
                webContext = webContext,
                userMessage = prompt,
                historial = ""
            )

            val safePrompt = guard.scanPrompt(fullPrompt)
            if (!safePrompt.isPermitted) {
                Log.w(TAG, "Prompt bloqueado por ConstitutionalGuard")
                return@withContext Result.success(safePrompt.response)
            }

            val response = runInference(safePrompt.text)

            val safeResponse = guard.scanResponse(response)
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
                Formato obligatorio: {"categoria": "CATEGORIA", "elemento": "elemento_detectado", "sentimiento": "GUSTA/NO_GUSTA"}
                Si no hay información nueva, responde únicamente: {}
                Texto: "$userMessage"
            """.trimIndent()

            val response = runInference(extractionPrompt)
            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "Error en generateJsonExtraction: ${e.message}", e)
            Result.failure(e)
        }
    }

    private suspend fun runInference(prompt: String): String {
        val engine = llmInference
        if (engine == null) {
            val msg = "Error: Modelo no cargado. Llama a loadModel() primero."
            Log.w(TAG, msg)
            return msg
        }
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Ejecutando inferencia con prompt de ${prompt.length} caracteres")
                val respuesta = engine.generateResponse(prompt)
                Log.d(TAG, "Inferencia completada: ${respuesta.length} caracteres de respuesta")
                respuesta
            } catch (e: Exception) {
                Log.e(TAG, "Error durante la inferencia: ${e.message}", e)
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
}
