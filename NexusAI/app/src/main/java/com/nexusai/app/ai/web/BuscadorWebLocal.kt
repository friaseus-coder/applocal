package com.nexusai.app.ai.web

import com.nexusai.app.data.model.ResultadoWeb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URLEncoder

class BuscadorWebLocal {

    suspend fun buscarEnWeb(consulta: String): List<ResultadoWeb> = withContext(Dispatchers.IO) {
        val listaResultados = mutableListOf<ResultadoWeb>()
        try {
            val queryCodificada = URLEncoder.encode(consulta, "UTF-8")
            val url = "https://html.duckduckgo.com/html/?q=$queryCodificada"

            val documento = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .timeout(8000)
                .get()

            val elementosHtml = documento.select(".web-result")

            for (elemento in elementosHtml.take(3)) {
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
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext listaResultados
    }

    suspend fun buscarComoContexto(consulta: String): String {
        val resultados = buscarEnWeb(consulta)
        if (resultados.isEmpty()) return ""

        return resultados.joinToString("\n") { resultado ->
            "Título: ${resultado.titulo}. Resumen: ${resultado.resumen}."
        }
    }
}
