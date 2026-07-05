package com.nexusai.app.domain.usecase

import com.nexusai.app.ai.web.BuscadorWebLocal
import com.nexusai.app.data.model.ResultadoWeb

class SearchWebUseCase(private val buscadorWeb: BuscadorWebLocal) {

    suspend operator fun invoke(consulta: String): List<ResultadoWeb> {
        return buscadorWeb.buscarEnWeb(consulta)
    }
}
