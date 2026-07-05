package com.nexusai.app.domain.llm

import android.content.Context
import com.nexusai.app.ai.security.ConstitutionalGuard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LocalInferenceEngine(private val context: Context) {

    private var isModelLoaded = false
    private var modelPath: String? = null
    private val guard = ConstitutionalGuard()

    suspend fun loadModel(modelFileName: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val modelFile = File(context.filesDir, modelFileName)
            if (!modelFile.exists()) {
                return@withContext Result.failure(
                    IllegalStateException("Model not found: $modelFileName")
                )
            }
            modelPath = modelFile.absolutePath
            isModelLoaded = true
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateResponse(
        prompt: String,
        perfilPrompt: String,
        ragContext: String = "",
        webContext: String = "",
        callback: (String) -> Unit
    ): Result<String> = withContext(Dispatchers.Default) {
        try {
            val fullPrompt = PromptBuilder.build(
                perfilPrompt = perfilPrompt,
                ragContext = ragContext,
                webContext = webContext,
                userMessage = prompt,
                historial = ""
            )

            val safePrompt = guard.scanPrompt(fullPrompt)
            if (!safePrompt.isPermitted) {
                return@withContext Result.success(safePrompt.response)
            }

            val response = runInference(safePrompt.text)

            val safeResponse = guard.scanResponse(response)
            callback(safeResponse)
            Result.success(safeResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateJsonExtraction(
        userMessage: String
    ): Result<String> = withContext(Dispatchers.Default) {
        try {
            val extractionPrompt = """
                Analiza este texto del usuario y extrae información sobre sus gustos, intereses, disgustos o valores. 
                Responde UNICAMENTE en formato JSON con la siguiente estructura: 
                {"categoria": "CATEGORIA", "elemento": "elemento_detectado", "sentimiento": "GUSTA/NO_GUSTA"}.
                Si no hay información nueva, responde únicamente: {}.
                Texto: "$userMessage"
            """.trimIndent()

            val result = runInference(extractionPrompt)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun runInference(prompt: String): String {
        return withContext(Dispatchers.IO) {
            if (!isModelLoaded) {
                return@withContext "El motor de IA no está inicializado. Por favor, descarga el modelo primero."
            }

            simulateLocalInference(prompt)
        }
    }

    private fun simulateLocalInference(prompt: String): String {
        return "[Inferencia Local - Simulación]\n" +
                "Prompt procesado: ${prompt.take(100)}..."
    }

    fun isReady(): Boolean = isModelLoaded

    fun unloadModel() {
        isModelLoaded = false
        modelPath = null
    }
}
