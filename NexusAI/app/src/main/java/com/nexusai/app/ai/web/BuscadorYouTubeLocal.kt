package com.nexusai.app.ai.web

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URLDecoder
import java.net.URLEncoder

data class VideoYouTube(
    val titulo: String,
    val url: String
)

class BuscadorYouTubeLocal {

    companion object {
        private const val TAG = "NexusAI_DebtMitigation"
        private val PATRON_YOUTUBE = Regex("""\[BUSCAR_YOUTUBE:\s*'(.*?)'\s*]""", RegexOption.DOT_MATCHES_ALL)

        fun extraerConsulta(texto: String): String? {
            val match = PATRON_YOUTUBE.find(texto)
            return match?.groupValues?.getOrNull(1)
        }

        fun limpiarRespuesta(texto: String): String {
            return texto.replace(PATRON_YOUTUBE, "").trim()
        }
    }

    suspend fun buscarVideos(consulta: String): List<VideoYouTube> = withContext(Dispatchers.IO) {
        val listaVideos = mutableListOf<VideoYouTube>()
        try {
            val consultaModificada = "site:youtube.com $consulta"
            val queryCodificada = URLEncoder.encode(consultaModificada, "UTF-8")
            val urlBusqueda = "https://html.duckduckgo.com/html/?q=$queryCodificada"

            val documento = try {
                Jsoup.connect(urlBusqueda)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(6000)
                    .get()
            } catch (e: HttpStatusException) {
                Log.w(TAG, "DuckDuckGo respondio con HTTP ${e.statusCode} en busqueda de YouTube: ${e.message}")
                return@withContext emptyList()
            } catch (e: IOException) {
                Log.w(TAG, "Error de red al buscar videos YouTube: ${e.message}")
                return@withContext emptyList()
            }

            val resultadosHtml = try {
                documento.select(".web-result")
            } catch (e: Exception) {
                Log.w(TAG, "Error al parsear CSS .web-result en busqueda YouTube (posible cambio de DOM): ${e.message}")
                return@withContext emptyList()
            }

            for (elemento in resultadosHtml.take(2)) {
                try {
                    val nodoTitulo = elemento.select(".result__title a").first()
                    val snippetUrl = nodoTitulo?.attr("href") ?: ""
                    if (nodoTitulo != null && snippetUrl.contains("youtube.com/watch")) {
                        val urlLimpia = try {
                            limpiarRedireccion(snippetUrl)
                        } catch (e: Exception) {
                            Log.w(TAG, "Error al limpiar URL de redireccion: ${e.message}")
                            snippetUrl
                        }
                        listaVideos.add(
                            VideoYouTube(
                                titulo = nodoTitulo.text().replace(" - YouTube", ""),
                                url = urlLimpia
                            )
                        )
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Error al extraer datos de un resultado de video: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error inesperado en busqueda YouTube: ${e.message}")
        }
        return@withContext listaVideos
    }

    private fun limpiarRedireccion(urlRedireccion: String): String {
        return if (urlRedireccion.contains("uddg=")) {
            val decoded = URLDecoder.decode(urlRedireccion, "UTF-8")
            val regex = "uddg=([^&]+)".toRegex()
            regex.find(decoded)?.groups?.get(1)?.value ?: urlRedireccion
        } else {
            urlRedireccion
        }
    }
}
