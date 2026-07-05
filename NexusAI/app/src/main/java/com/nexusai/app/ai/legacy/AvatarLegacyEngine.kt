package com.nexusai.app.ai.legacy

import android.content.Context
import android.net.Uri
import android.util.Log
import com.nexusai.app.ai.rag.EmbeddingEngine
import com.nexusai.app.data.local.dao.MemoriaPerfilDao
import com.nexusai.app.data.local.entity.MemoriaPerfilEntity
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Motor de procesamiento de documentos legado (PDF y TXT) para la
 * creación de Avatars Personalizados.
 *
 * Convierte documentos del usuario en fragmentos semánticos indexados
 * con vectores de embedding para búsqueda RAG.
 */
class AvatarLegacyEngine(
    private val context: Context,
    private val memoriaDao: MemoriaPerfilDao,
    private val embeddingEngine: EmbeddingEngine
) {

    companion object {
        private const val TAG = "NexusAI_Legacy"

        /** Longitud máxima por fragmento semántico */
        private const val TAMANO_CHUNK = 400
    }

    init {
        // Inicialización obligatoria de PDFBox para carga de fuentes nativas
        PDFBoxResourceLoader.init(context)
        Log.d(TAG, "PDFBoxResourceLoader inicializado — fuentes nativas listas")
    }

    // ------------------------------------------------------------------
    // Procesamiento principal del documento
    // ------------------------------------------------------------------

    /**
     * Procesa un documento (PDF o TXT) seleccionado por el usuario:
     * 1. Detecta el tipo de archivo por MIME o extensión
     * 2. Extrae el texto según el formato
     * 3. Segmenta en fragmentos semánticos (chunks)
     * 4. Genera embeddings ONNX para cada fragmento
     * 5. Almacena todo en Room (reemplazando datos previos del perfil)
     *
     * @param uri Uri del documento seleccionado por el usuario
     * @param perfilId ID del perfil IA al que pertenecerán las memorias
     * @param chunkSize Tamaño máximo en caracteres por fragmento
     * @return Result con la cantidad de fragmentos generados, o failure con descripción
     */
    suspend fun procesarDocumento(
        uri: Uri,
        perfilId: Long,
        chunkSize: Int = TAMANO_CHUNK
    ): Result<Int> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Iniciando procesamiento de documento — Uri: $uri")

            // --- 1. Detección dinámica del tipo de archivo ---
            val tipoMime = context.contentResolver.getType(uri) ?: ""
            val nombreArchivo = obtenerNombreArchivo(uri)
            val extension = nombreArchivo.substringAfterLast('.', "").lowercase()

            Log.d(TAG, "Archivo: \"$nombreArchivo\" | MIME: $tipoMime | Extensión: $extension")

            // --- 2. Extracción de texto según formato ---
            val texto = when {
                extension == "pdf" || tipoMime.contains("pdf") -> extraerTextoPdf(uri)
                extension == "txt" || tipoMime.contains("text") -> extraerTextoPlano(uri)
                else -> {
                    val msg = "Formato no soportado: \"$extension\" ($tipoMime). Solo se aceptan PDF y TXT."
                    Log.e(TAG, msg)
                    return@withContext Result.failure(Exception(msg))
                }
            }

            if (texto.isBlank()) {
                val msg = "El documento no contiene texto extraíble. " +
                        "Puede estar protegido por contraseña, ser un PDF escaneado (solo imágenes) o estar vacío."
                Log.w(TAG, msg)
                return@withContext Result.failure(Exception(msg))
            }

            Log.d(TAG, "Texto extraído correctamente — ${texto.length} caracteres totales")

            // --- 3. Segmentación en fragmentos semánticos ---
            val chunks = segmentarTexto(texto, chunkSize)
            Log.d(TAG, "Texto segmentado en ${chunks.size} fragmentos de hasta $chunkSize caracteres")

            // --- 4. Generación de vectores de embedding ---
            val embeddings = embeddingEngine.generateEmbeddings(chunks)
            Log.d(TAG, "Embeddings ONNX generados: ${embeddings.size} vectores de ${embeddings.firstOrNull()?.size ?: 0} dimensiones")

            // --- 5. Preparar entidades para Room ---
            val memorias = chunks.mapIndexed { indice, fragmento ->
                MemoriaPerfilEntity(
                    perfilId = perfilId,
                    textoFragmento = fragmento,
                    vectorSerialized = embeddings[indice].joinToString(",")
                )
            }

            // --- 6. Reemplazar datos previos e insertar nuevos ---
            memoriaDao.deleteByPerfil(perfilId)
            memoriaDao.insertAll(memorias)

            Log.d(TAG, "Documento procesado exitosamente: ${memorias.size} fragmentos guardados en Room para el perfil $perfilId")
            Result.success(chunks.size)

        } catch (e: Exception) {
            Log.e(TAG, "Error crítico al procesar documento: ${e.message}", e)
            Result.failure(e)
        }
    }

    // ------------------------------------------------------------------
    // Extracción de texto por formato
    // ------------------------------------------------------------------

    /**
     * Extrae texto de un archivo PDF usando PDFBox.
     * Soporta documentos de varias páginas con ordenamiento posicional.
     * Cierra automáticamente el flujo y el documento con .use {}.
     *
     * @throws IllegalStateException si no se puede abrir el stream
     * @return Texto completo extraído del PDF
     */
    private fun extraerTextoPdf(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("No se pudo abrir el archivo PDF. " +
                    "El ContentResolver devolvió null para la Uri proporcionada.")

        return inputStream.use { stream ->
            PDDocument.load(stream).use { documento ->
                val totalPaginas = documento.numberOfPages
                Log.d(TAG, "PDF cargado correctamente — $totalPaginas páginas detectadas")

                // Verificar si el PDF está encriptado (protegido por contraseña)
                if (documento.isEncrypted) {
                    val msg = "El PDF está protegido por contraseña y no se puede extraer su contenido."
                    Log.e(TAG, msg)
                    throw SecurityException(msg)
                }

                val stripper = PDFTextStripper()
                stripper.sortByPosition = true  // Orden lógico de lectura
                stripper.getText(documento).also { texto ->
                    Log.d(TAG, "Texto extraído del PDF — ${texto.length} caracteres en $totalPaginas páginas")
                }
            }
        }
    }

    /**
     * Extrae texto de un archivo de texto plano (.txt).
     * Usa el lector de caracteres estándar de Kotlin con codificación UTF-8.
     *
     * @throws IllegalStateException si no se puede abrir el stream
     * @return Contenido completo del archivo de texto
     */
    private fun extraerTextoPlano(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("No se pudo abrir el archivo de texto. " +
                    "El ContentResolver devolvió null para la Uri proporcionada.")

        return BufferedReader(InputStreamReader(inputStream)).use { lector ->
            lector.readText().also { texto ->
                Log.d(TAG, "Archivo de texto plano cargado — ${texto.length} caracteres")
            }
        }
    }

    // ------------------------------------------------------------------
    // Utilidades
    // ------------------------------------------------------------------

    /**
     * Obtiene el nombre legible del archivo desde el ContentResolver
     * usando el estándar OpenableColumns.DISPLAY_NAME.
     */
    private fun obtenerNombreArchivo(uri: Uri): String {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            if (it.moveToFirst()) {
                val indice = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (indice >= 0) it.getString(indice) else uri.lastPathSegment ?: "desconocido"
            } else {
                uri.lastPathSegment ?: "desconocido"
            }
        } ?: uri.lastPathSegment ?: "desconocido"
    }

    // ------------------------------------------------------------------
    // Segmentación semántica
    // ------------------------------------------------------------------

    /**
     * Divide un texto largo en fragmentos semánticos (chunks) respetando
     * los límites de palabras y oraciones.
     *
     * Algoritmo:
     * 1. Limpia espacios múltiples y saltos de página
     * 2. Divide por párrafos (doble salto de línea)
     * 3. Agrupa párrafos hasta alcanzar el tamaño máximo
     * 4. Si un párrafo excede el tamaño, lo divide por oraciones
     * 5. Garantiza que ningún corte parta una palabra a la mitad
     *
     * @param texto Texto limpio pre-procesado
     * @param tamanoMaximo Caracteres máximos por fragmento
     * @return Lista de fragmentos de texto
     */
    private fun segmentarTexto(texto: String, tamanoMaximo: Int): List<String> {
        // 1. Normalizar el texto: eliminar saltos de página y espacios redundantes
        val limpio = texto
            .replace(Regex("""\r\n"""), "\n")               // Normalizar CR+LF → LF
            .replace(Regex("""[ \t]+"""), " ")               // Múltiples espacios → uno
            .replace(Regex("""\n{3,}"""), "\n\n")            // Múltiples saltos → doble salto
            .replace(Regex("""\f"""), "\n")                  // Saltos de página → salto de línea
            .replace(Regex("""[ \t]+\n"""), "\n")            // Espacios al final de línea
            .trim()

        val fragmentos = mutableListOf<String>()
        val parrafos = limpio.split(Regex("""\n\n"""))
        val acumulador = StringBuilder()

        /**
         * Cierra el fragmento actual y lo agrega a la lista,
         * limpiando espacios sobrantes.
         */
        fun cerrarFragmento() {
            val contenido = acumulador.toString().trim()
            if (contenido.isNotBlank()) {
                fragmentos.add(contenido)
            }
            acumulador.clear()
        }

        for (parrafo in parrafos) {
            val parrafoLimpio = parrafo.trim()
            if (parrafoLimpio.isBlank()) continue

            // Si el párrafo actual no cabe en el fragmento actual, cerrar el fragmento
            if (acumulador.isNotEmpty() &&
                acumulador.length + parrafoLimpio.length + 1 > tamanoMaximo
            ) {
                cerrarFragmento()
            }

            // Si el párrafo individual es más largo que el tamaño máximo,
            // dividirlo por límites de oración
            if (parrafoLimpio.length > tamanoMaximo) {
                // Si hay algo acumulado, cerrar primero
                if (acumulador.isNotEmpty()) cerrarFragmento()

                // Dividir el párrafo largo por oraciones
                val oraciones = parrafoLimpio.split(Regex("""(?<=[.!?])\s+"""))
                val oracionesAcumuladas = mutableListOf<String>()
                var longitudOraciones = 0

                for (oracion in oraciones) {
                    if (longitudOraciones + oracion.length + 1 > tamanoMaximo &&
                        oracionesAcumuladas.isNotEmpty()
                    ) {
                        // Cerrar grupo actual de oraciones
                        fragmentos.add(oracionesAcumuladas.joinToString(" ").trim())
                        oracionesAcumuladas.clear()
                        longitudOraciones = 0
                    }
                    oracionesAcumuladas.add(oracion)
                    longitudOraciones += oracion.length + 1
                }

                // Último grupo de oraciones
                if (oracionesAcumuladas.isNotEmpty()) {
                    fragmentos.add(oracionesAcumuladas.joinToString(" ").trim())
                }
            } else {
                // Párrafo normal: agregar al acumulador
                if (acumulador.isNotEmpty()) acumulador.append("\n\n")
                acumulador.append(parrafoLimpio)
            }
        }

        // Último fragmento pendiente
        cerrarFragmento()

        return fragmentos
    }
}
