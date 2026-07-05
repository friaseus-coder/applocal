package com.nexusai.app.domain.usecase

import android.net.Uri
import com.nexusai.app.ai.legacy.AvatarLegacyEngine

class CargaMemoriaUseCase(private val legacyEngine: AvatarLegacyEngine) {

    suspend operator fun invoke(uri: Uri, perfilId: Long): Result<Int> {
        return legacyEngine.procesarDocumento(uri, perfilId)
    }
}
