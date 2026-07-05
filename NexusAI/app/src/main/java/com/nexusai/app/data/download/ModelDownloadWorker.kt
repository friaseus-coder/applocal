package com.nexusai.app.data.download

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class ModelDownloadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "NexusAI_DebtMitigation"
        private const val BUFFER_SIZE = 8192
        const val KEY_PROGRESO = "progreso"
        const val KEY_MODEL_URL = "MODEL_URL"
        const val KEY_MODEL_FILE_NAME = "MODEL_FILE_NAME"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val url = inputData.getString(KEY_MODEL_URL)
        val fileName = inputData.getString(KEY_MODEL_FILE_NAME) ?: "gemma-2-2b-it-cpu-int4.bin"

        if (url == null) {
            Log.e(TAG, "URL del modelo no proporcionada — abortando descarga")
            return@withContext Result.failure()
        }

        val outputFile = File(applicationContext.filesDir, fileName)
        Log.d(TAG, "Iniciando descarga del modelo desde $url hacia ${outputFile.absolutePath}")

        try {
            URL(url).openConnection().let { connection ->
                connection.connect()
                val contentLength = connection.contentLengthLong
                val inputStream = connection.getInputStream()
                val outputStream = FileOutputStream(outputFile)
                val buffer = ByteArray(BUFFER_SIZE)
                var bytesRead: Int
                var totalBytesRead = 0L

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    totalBytesRead += bytesRead
                    if (contentLength > 0) {
                        val progreso = ((totalBytesRead * 100) / contentLength).toInt()
                        setProgress(workDataOf(KEY_PROGRESO to progreso))
                    }
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()
            }

            setProgress(workDataOf(KEY_PROGRESO to 100))
            Log.d(TAG, "Modelo descargado exitosamente: ${outputFile.absolutePath} (${outputFile.length()} bytes)")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error durante la descarga del modelo: ${e.message}", e)
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
