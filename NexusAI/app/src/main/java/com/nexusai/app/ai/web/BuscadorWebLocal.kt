package com.nexusai.app.ai.web

import android.util.Log
import com.nexusai.app.data.model.ResultadoWeb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URLEncoder

class BuscadorWebLocal {

    companion object {
        private const val TAG = "NexusAI_DebtMitigation"
    }

    suspend fun buscarEnWeb(consulta: String): List<ResultadoWeb> = withContext(Dispatchers.IO) {
        val listaResultados = mutableListOf<ResultadoWeb>()
        try {
            val queryCodificada = URLEncoder.encode(consulta, "UTF-8")
            val url = "https://html.duckduckgo.com/html/?q=$queryCodificada"

            val documento = try {
                Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(8000)
                    .get()
            } catch (e: HttpStatusException) {
                Log.w(TAG, "DuckDuckGo respondio con HTTP ${e.statusCode}: ${e.message}")
                return@withContext emptyList()
            } catch (e: IOException) {
                Log.w(TAG, "Error de red al conectar con DuckDuckGo: ${e.message}")
                return@withContext emptyList()
            }

            val elementosHtml = try {
                documento.select(".web-result")
            } catch (e: Exception) {
                Log.w(TAG, "Error al parsear CSS selector .web-result (posible cambio de DOM): ${e.message}")
                return@withContext emptyList()
            }

            for (elemento in elementosHtml.take(3)) {
                try {
                    val nodoTitulo = elemento.select(".result__title a").first()
                    val nodoSnippet = elemento.select(".result__snippet").first()
                    if (nodoTitulo != null && nodoSnippet != null) {
                        listaResultados.add(
                            ResultadoWeb(
                                titulo = nodoTitulo.text(),
                                resumen = nodoSnippet.text()
                            )
                        )
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Error al extraer campos de un resultado individual: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error inesperado en busqueda web: ${e.message}")
        }
        return@withContext listaResultados
    }

    suspend fun buscarComoContexto(consulta: String): String {
        val resultados = buscarEnWeb(consulta)
        if (resultados.isEmpty()) return ""
        return resultados.joinToString("\n") { resultado ->
            "Titulo: ${resultado.titulo}. Resumen: ${resultado.resumen}."
        }
    }
}
