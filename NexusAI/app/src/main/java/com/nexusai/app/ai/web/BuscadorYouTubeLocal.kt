package com.nexusai.app.ai.web

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URLDecoder
import java.net.URLEncoder

data class VideoYouTube(
    val titulo: String,
    val url: String
)

class BuscadorYouTubeLocal {

    suspend fun buscarVideos(consulta: String): List<VideoYouTube> = withContext(Dispatchers.IO) {
        val listaVideos = mutableListOf<VideoYouTube>()
        try {
            val consultaModificada = "site:youtube.com $consulta"
            val queryCodificada = URLEncoder.encode(consultaModificada, "UTF-8")
            val urlBusqueda = "https://html.duckduckgo.com/html/?q=$queryCodificada"

            val documento = Jsoup.connect(urlBusqueda)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(6000)
                .get()

            val resultadosHtml = documento.select(".web-result")

            for (elemento in resultadosHtml.take(2)) {
                val nodoTitulo = elemento.select(".result__title a").first()
                val snippetUrl = nodoTitulo?.attr("href") ?: ""

                if (nodoTitulo != null && snippetUrl.contains("youtube.com/watch")) {
                    val urlLimpia = limpiarRedireccion(snippetUrl)
                    listaVideos.add(
                        VideoYouTube(
                            titulo = nodoTitulo.text().replace(" - YouTube", ""),
                            url = urlLimpia
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    companion object {
        private val PATRON_YOUTUBE = Regex("""\[BUSCAR_YOUTUBE:\s*'(.*?)'\s*]""", RegexOption.DOT_MATCHES_ALL)

        fun extraerConsulta(texto: String): String? {
            val match = PATRON_YOUTUBE.find(texto)
            return match?.groupValues?.getOrNull(1)
        }

        fun limpiarRespuesta(texto: String): String {
            return texto.replace(PATRON_YOUTUBE, "").trim()
        }
    }
}
