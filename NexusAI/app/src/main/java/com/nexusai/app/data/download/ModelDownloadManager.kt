package com.nexusai.app.data.download

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.lifecycle.Observer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

data class DownloadState(
    val isRunning: Boolean = false,
    val progreso: Int = 0,
    val isCompletado: Boolean = false,
    val isError: Boolean = false
)

class ModelDownloadManager(private val context: Context) {

    companion object {
        private const val TAG = "NexusAI_DebtMitigation"
        private const val WORK_NAME = "modelo_llm_download"
    }

    private val workManager = WorkManager.getInstance(context)

    fun descargarModelo(
        url: String,
        fileName: String = "gemma-2-2b-it-cpu-int4.bin"
    ) {
        Log.d(TAG, "Encolando descarga del modelo: $url")

        val inputData = Data.Builder()
            .putString(ModelDownloadWorker.KEY_MODEL_URL, url)
            .putString(ModelDownloadWorker.KEY_MODEL_FILE_NAME, fileName)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val request = OneTimeWorkRequestBuilder<ModelDownloadWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .addTag(WORK_NAME)
            .build()

        workManager.enqueueUniqueWork(
            WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun observarEstado(): Flow<DownloadState> {
        val liveData = workManager.getWorkInfosForUniqueWorkLiveData(WORK_NAME)
        return callbackFlow {
            val observer = Observer<List<WorkInfo>> { workInfos ->
                val info = workInfos.firstOrNull()
                if (info != null) {
                    trySend(mapearEstado(info))
                }
            }
            liveData.observeForever(observer)
            awaitClose { liveData.removeObserver(observer) }
        }
    }

    fun cancelarDescarga() {
        workManager.cancelUniqueWork(WORK_NAME)
        Log.d(TAG, "Descarga cancelada por el usuario")
    }

    private fun mapearEstado(info: WorkInfo): DownloadState {
        val progreso = info.progress.getInt(ModelDownloadWorker.KEY_PROGRESO, 0)
        return when (info.state) {
            WorkInfo.State.RUNNING -> DownloadState(
                isRunning = true,
                progreso = progreso
            )
            WorkInfo.State.SUCCEEDED -> DownloadState(
                isCompletado = true,
                progreso = 100
            )
            WorkInfo.State.FAILED -> DownloadState(
                isError = true,
                progreso = progreso
            )
            WorkInfo.State.CANCELLED -> DownloadState(
                isError = true
            )
            else -> DownloadState()
        }
    }
}
