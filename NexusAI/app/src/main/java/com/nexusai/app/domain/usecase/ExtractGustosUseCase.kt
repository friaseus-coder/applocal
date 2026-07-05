package com.nexusai.app.domain.usecase

import com.nexusai.app.data.repository.GustoRepository
import com.nexusai.app.domain.llm.LocalInferenceEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ExtractGustosUseCase(
    private val gustoRepository: GustoRepository,
    private val inferenceEngine: LocalInferenceEngine
) {

    suspend operator fun invoke(userMessage: String) = withContext(Dispatchers.Default) {
        try {
            val result = inferenceEngine.generateJsonExtraction(userMessage)
            result.onSuccess { jsonString ->
                if (jsonString.isBlank() || jsonString == "{}") return@onSuccess

                val json = JSONObject(jsonString)
                if (json.has("categoria") && json.has("elemento") && json.has("sentimiento")) {
                    gustoRepository.insertGusto(
                        categoria = json.getString("categoria"),
                        elemento = json.getString("elemento"),
                        sentimiento = json.getString("sentimiento")
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
