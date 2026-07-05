# Documento Técnico — Nexus AI

**Versión:** 1.0.0  
**Arquitectura:** Clean Architecture + MVVM  
**Lenguaje:** Kotlin 2.1.0  
**SDK Mínimo:** Android 8.0 (API 26)  
**SDK Compilación:** Android 15 (API 35)  
**IDE:** Android Studio  

---

## 1. Resumen del Proyecto

Nexus AI es una aplicación Android nativa que implementa un asistente de inteligencia artificial completamente offline. La aplicación ejecuta un modelo de lenguaje local (LLM) en el dispositivo, utiliza RAG (Retrieval-Augmented Generation) para búsqueda semántica sobre conocimiento local, e incorpora un sistema de seguridad constitucional basado en la DUDH.

---

## 2. Estructura del Proyecto

```
NexusAI/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── gradle/
│   └── libs.versions.toml
└── app/
    ├── build.gradle.kts
    ├── proguard-rules.pro
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/
        │   ├── values/
        │   │   ├── strings.xml
        │   │   └── themes.xml
        │   └── xml/
        │       └── data_extraction_rules.xml
        └── java/com/nexusai/app/
            ├── NexusAIApp.kt
            ├── MainActivity.kt
            ├── data/
            │   ├── model/
            │   │   ├── PerfilIA.kt
            │   │   ├── MensajeChat.kt
            │   │   └── ResultadoWeb.kt
            │   ├── local/
            │   │   ├── AppDatabase.kt
            │   │   ├── converter/Converters.kt
            │   │   ├── entity/
            │   │   │   ├── PerfilIAEntity.kt
            │   │   │   ├── GustoUsuarioEntity.kt
            │   │   │   ├── MemoriaPerfilEntity.kt
            │   │   │   └── MensajeChatEntity.kt
            │   │   └── dao/
            │   │       ├── PerfilIADao.kt
            │   │       ├── GustoUsuarioDao.kt
            │   │       ├── MemoriaPerfilDao.kt
            │   │       └── MensajeChatDao.kt
            │   └── repository/
            │       ├── PerfilRepository.kt
            │       ├── GustoRepository.kt
            │       ├── MemoriaRepository.kt
            │       └── ChatRepository.kt
            ├── domain/
            │   ├── llm/
            │   │   ├── LocalInferenceEngine.kt
            │   │   └── PromptBuilder.kt
            │   └── usecase/
            │       ├── GetPerfilesUseCase.kt
            │       ├── SelectPerfilUseCase.kt
            │       ├── SendMessageUseCase.kt
            │       ├── ExtractGustosUseCase.kt
            │       ├── SearchWebUseCase.kt
            │       └── CargaMemoriaUseCase.kt
            ├── ai/
            │   ├── web/
            │   │   └── BuscadorWebLocal.kt
            │   ├── rag/
            │   │   ├── EmbeddingEngine.kt
            │   │   ├── VectorDatabase.kt
            │   │   └── RAGRetriever.kt
            │   ├── security/
            │   │   └── ConstitutionalGuard.kt
            │   └── legacy/
            │       └── AvatarLegacyEngine.kt
            └── ui/
                ├── theme/
                │   ├── Color.kt
                │   ├── Theme.kt
                │   └── Type.kt
                ├── util/
                │   └── AnimationUtils.kt
                ├── navigation/
                │   └── NavGraph.kt
                ├── screen/
                │   ├── persona/
                │   │   ├── PersonaSelectionScreen.kt
                │   │   └── PersonaSelectionViewModel.kt
                │   ├── chat/
                │   │   ├── ChatScreen.kt
                │   │   └── ChatViewModel.kt
                │   └── memory/
                │       ├── MemoryScreen.kt
                │       └── MemoryViewModel.kt
                └── components/
                    ├── PersonaCard.kt
                    ├── ChatBubble.kt
                    ├── InputBar.kt
                    ├── GlassCard.kt
                    ├── MemoryCard.kt
                    └── BottomNavBar.kt
```

---

## 3. Arquitectura General

```
┌──────────────────────────────────────────────────────────┐
│                     UI LAYER                              │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐         │
│  │ Persona    │  │ Chat       │  │ Memory     │         │
│  │ Screen     │  │ Screen     │  │ Screen     │         │
│  └─────┬──────┘  └─────┬──────┘  └─────┬──────┘         │
│  ┌─────┴──────┐  ┌─────┴──────┐  ┌─────┴──────┐         │
│  │ Persona    │  │ Chat       │  │ Memory     │         │
│  │ ViewModel  │  │ ViewModel  │  │ ViewModel  │         │
│  └────────────┘  └─────┬──────┘  └────────────┘         │
├────────────────────────┼─────────────────────────────────┤
│                    DOMAIN LAYER                           │
│  ┌──────────┐  ┌───────┴───────┐  ┌────────────────┐    │
│  │ Use Cases│◄─┤ PromptBuilder │  │ LocalInference │    │
│  │(6 casos) │  │               │  │   Engine       │    │
│  └────┬─────┘  └───────────────┘  └───────┬────────┘    │
├───────┼────────────────────────────────────┼─────────────┤
│       │              AI LAYER              │             │
│  ┌────┴────────────────────────────────────┴──────────┐  │
│  │ BuscadorWebLocal / RAGRetriever / Constitutional   │  │
│  │ EmbeddingEngine / VectorDatabase / AvatarLegacy     │  │
│  └───────────────────────┬────────────────────────────┘  │
├──────────────────────────┼───────────────────────────────┤
│                     DATA LAYER                           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐│
│  │Entities  │  │ DAOs     │  │Room DB   │  │Repos     ││
│  │(4 Room)  │  │(4 DAO)   │  │(SQLite)  │  │(4 repos) ││
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘│
└──────────────────────────────────────────────────────────┘
```

### Capas

| Capa | Paquete | Responsabilidad |
|---|---|---|
| **UI** | `com.nexusai.app.ui` | Jetpack Compose, ViewModels, componentes, navegación |
| **Domain** | `com.nexusai.app.domain` | Casos de uso, lógica de negocio, LLM, prompts |
| **AI** | `com.nexusai.app.ai` | Motores especializados: búsqueda web, RAG, seguridad, legado |
| **Data** | `com.nexusai.app.data` | Room DB, entidades, DAOs, repositorios, modelos de datos |

---

## 4. Base de Datos (Room)

### Esquema Entidad-Relación

```
+------------------+       +---------------------+
|   perfiles_ia    |       |  historial_mensajes  |
+------------------+       +---------------------+
| PK id: Long      |       | PK id: Long         |
| nombre: String   |◄──────┤ FK perfilId: Long   |
| tipo: String     | 1:N   | remitente: String   |
| descripcion      |       | contenido: String   |
| icono: String    |       | timestamp: Long     |
| promptSistemaBase|       +---------------------+
| tagsRag: String  |
| isActive: Boolean|       +---------------------+
+------------------+       |   memorias_perfil    |
        │                  +---------------------+
        │ 1:N              | PK id: Long         |
        └──────────────────┤ FK perfilId: Long   |
                           | textoFragmento: Str |
                           | vectorSerialized: ? |
                           +---------------------+

+---------------------+
|   gustos_usuario    |
+---------------------+
| PK id: Long         |
| categoria: String   |
| elemento: String    |
| sentimiento: String |
| fechaDescubrimiento |
+---------------------+
```

### Tablas

#### `perfiles_ia` — Perfiles de personalidad

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | `Long` PK | Auto-generado |
| `nombre` | `String` | Nombre del perfil (ej: "El Estoico") |
| `tipo` | `String` | Identificador del arquetipo (ej: "ESTOICO") |
| `descripcion` | `String` | Descripción breve del propósito |
| `icono` | `String` | Identificador del icono Material Design |
| `promptSistemaBase` | `String` | Prompt de sistema que define la personalidad |
| `tagsRag` | `String` | Etiqueta del área de conocimiento RAG |
| `isActive` | `Boolean` | Indica si es el perfil activo (solo 1 activo) |

#### `historial_mensajes` — Historial de conversaciones

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | `Long` PK | Auto-generado |
| `perfilId` | `Long` FK → `perfiles_ia.id` | Perfil asociado (CASCADE on delete) |
| `remitente` | `String` | `"USUARIO"` o `"IA"` |
| `contenido` | `String` | Texto del mensaje |
| `timestamp` | `Long` | Marca de tiempo en milisegundos |

Índice: `perfilId`.

#### `memorias_perfil` — Fragmentos RAG por perfil

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | `Long` PK | Auto-generado |
| `perfilId` | `Long` FK → `perfiles_ia.id` | Perfil asociado (CASCADE on delete) |
| `textoFragmento` | `String` | Fragmento de texto (~300 caracteres) |
| `vectorSerialized` | `String?` | Vector 384-dim serializado como CSV |

Índice: `perfilId`.

#### `gustos_usuario` — Preferencias aprendidas del usuario

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | `Long` PK | Auto-generado |
| `categoria` | `String` | Categoría (ej: "COMIDA", "VALOR_MORAL") |
| `elemento` | `String` | Elemento detectado (ej: "Café espresso") |
| `sentimiento` | `String` | `"GUSTA"`, `"NO_GUSTA"`, `"NEUTRAL"` |
| `fechaDescubrimiento` | `Long` | Timestamp de creación |

### DAOs

| DAO | Métodos Principales |
|---|---|
| `PerfilIADao` | `getAllPerfiles()`, `getActivePerfil()`, `deactivateAll()`, `activatePerfil()` |
| `MensajeChatDao` | `getMensajesByPerfil()`, `getRecentMensajes()`, `insert()`, `deleteByPerfil()`, `deleteAll()` |
| `MemoriaPerfilDao` | `getMemoriasByPerfil()`, `insertAll()`, `deleteByPerfil()`, `getTopMemorias()` |
| `GustoUsuarioDao` | `getAllGustos()`, `getGustosByCategoria()`, `insert()`, `delete()`, `deleteAll()`, `searchByElemento()` |

---

## 5. Repositorios

| Repositorio | Fuente | Función |
|---|---|---|
| `PerfilRepository` | `PerfilIADao` | CRUD perfiles, activación, mapeo Entity → Domain |
| `ChatRepository` | `MensajeChatDao` | Envío y recuperación de mensajes |
| `MemoriaRepository` | `MemoriaPerfilDao` | Gestión de fragmentos RAG (sin vectores) |
| `GustoRepository` | `GustoUsuarioDao` | CRUD de gustos del usuario |

Cada repositorio expone `Flow` para datos reactivos y funciones `suspend` para operaciones de escritura. Los `Entity` de Room se mapean a modelos de dominio (`PerfilIA`, `MensajeChat`, `GustoUsuario`) dentro de cada repositorio.

---

## 6. Capa de IA

### 6.1 BuscadorWebLocal (`ai/web/BuscadorWebLocal.kt`)

Búsqueda web gratuita mediante scraping de DuckDuckGo HTML.

```
Flujo:
1. Usuario activa búsqueda web → `buscarComoContexto(consulta)`
2. Codifica consulta → URLEncoder.encode()
3. GET a https://html.duckduckgo.com/html/?q=...
4. User-Agent: Chrome 120 (para evitar bloqueos)
5. Timeout: 8 segundos
6. Parsea .web-result → .result__title a + .result__snippet
7. Retorna top 3 resultados como List<ResultadoWeb>
8. Formatea como string de contexto para inyección en prompt
```

**Ventaja**: Coste $0, sin API key, sin servidor proxy.

### 6.2 Sistema RAG (`ai/rag/`)

#### EmbeddingEngine (`EmbeddingEngine.kt`)

Genera vectores de 384 dimensiones usando un enfoque hash-based (sin red neuronal):

```
fun generateEmbedding(text: String): FloatArray {
    1. Normalizar: lowercase + trim
    2. Tokenizar por whitespace
    3. Para cada palabra (índice i):
        hash = word.hashCode()
        idx = (i * 31 + hash) % 384
        vector[idx] += 1.0 / totalWords
    4. Normalizar L2 (dividir por magnitud euclidiana)
}
```

| Método | Descripción |
|---|---|
| `generateEmbedding(text)` | Vector 384-D para un texto |
| `generateEmbeddings(texts)` | Batch de vectores para lista de textos |
| `cosineSimilarity(a, b)` | Similitud coseno entre dos vectores |

#### VectorDatabase (`VectorDatabase.kt`)

Base de datos vectorial en memoria con `Mutex` para thread-safety.

| Método | Descripción |
|---|---|
| `insert(record)` | Inserta un `VectorRecord` (id, perfilId, text, vector) |
| `insertAll(records)` | Inserción batch |
| `search(queryVector, perfilId, topK)` | Búsqueda por similitud coseno filtrada por perfil |
| `deleteByPerfil(perfilId)` | Elimina todos los registros de un perfil |
| `count()` | Número total de registros |

#### RAGRetriever (`RAGRetriever.kt`)

Orquesta la recuperación semántica:

```
1. Obtener todas las memorias del perfil desde Room (memoriaDao)
2. Generar embedding de la consulta del usuario
3. Para cada memoria, generar embedding del fragmento
4. Calcular similitud coseno
5. Ordenar descendente y tomar top-K (default 3)
6. Formatear como contexto: "- \"fragmento\""
```

### 6.3 ConstitutionalGuard (`ai/security/ConstitutionalGuard.kt`)

Sistema de seguridad basado en la Declaración Universal de Derechos Humanos.

**Directiva constitucional**: Texto inyectado al inicio de cada prompt, por encima del prompt de personalidad. Contiene:
- Respeto absoluto a la dignidad humana (Art. 1 y 2 DUDH)
- Coexistencia y libertad de pensamiento (Art. 18 DUDH)
- Prohibición de odio, discriminación, violencia

**Patrones prohibidos** (regex):
- `odio\s+(a|hacia|racial|étnico|religioso)`
- `discurso\s+de\s+odio`
- `violencia\s+(racial|étnica|religiosa|de género)`
- `superioridad\s+(racial|étnica|religiosa)`
- `limpieza\s+étnica`
- `genocidio`

**Doble filtrado**:
1. `scanPrompt(prompt)` → Si el prompt contiene patrones prohibidos, retorna `RESPUESTA_RECHAZO` sin ejecutar inferencia.
2. `scanResponse(response)` → Si la respuesta generada contiene patrones prohibidos, la reemplaza por `RESPUESTA_RECHAZO`.

### 6.4 AvatarLegacyEngine (`ai/legacy/AvatarLegacyEngine.kt`)

Procesa documentos para crear perfiles personalizados:

```
procesarDocumento(uri, perfilId, chunkSize=300):
1. Leer texto desde URI (contentResolver.openInputStream)
2. Dividir en párrafos por "\n\n"
3. Agrupar párrafos en chunks de ~300 caracteres
4. Generar embeddings para cada chunk
5. Eliminar memorias existentes del perfil
6. Insertar nuevos chunks con vectores serializados
```

---

## 7. Motor de Inferencia Local

### LocalInferenceEngine (`domain/llm/LocalInferenceEngine.kt`)

```kotlin
class LocalInferenceEngine(private val context: Context) {
    private var isModelLoaded = false
    private var modelPath: String? = null
    private val guard = ConstitutionalGuard()
}
```

| Método | Descripción |
|---|---|
| `loadModel(modelFileName)` | Verifica existencia del archivo .bin/.gguf en `context.filesDir` |
| `generateResponse(prompt, perfilPrompt, ragContext, webContext, callback)` | Pipeline completa: build prompt → scan → inferencia → scan → callback |
| `generateJsonExtraction(userMessage)` | Prompt especializado para extraer datos JSON de gustos |
| `isReady()` | Estado del modelo |
| `unloadModel()` | Libera recursos |

**Pipeline de `generateResponse`**:
```
1. PromptBuilder.build() → ensambla: perfilPrompt + ragContext + webContext + historial + mensaje
2. guard.scanPrompt(fullPrompt) → verifica seguridad
3. Si no permitido → retorna respuesta de rechazo
4. runInference(safePrompt) → ejecuta el modelo local
5. guard.scanResponse(response) → verifica seguridad de la respuesta
6. callback(response) → guarda respuesta en BD, extrae gustos en segundo plano
```

**Estado actual**: `simulateLocalInference()` es un stub que retorna un mensaje de simulación. La integración real con MediaPipe/llama.cpp vía JNI está preparada para conectarse en `runInference()`.

### PromptBuilder (`domain/llm/PromptBuilder.kt`)

Ensamblador de prompts con dos modos:

**Modo normal** (`build`):
```
[perfilPrompt]
[CONTEXTO RAG LOCAL] (si existe)
[INFORMACIÓN RECIENTE DE INTERNET] (si búsqueda web activada)
[HISTORIAL RECIENTE] (últimos mensajes)
[MENSAJE DEL USUARIO]
Usuario: {mensaje}
Respuesta:
```

**Modo legado** (`buildForLegacyAvatar`):
```
[IDENTIDAD DEL COMPAÑERO]
Actúas como {nombre}. Hablas en primera persona.
[FRAGMENTOS DE MEMORIAS RECUPERADOS DEL RAG LOCAL]
- "fragmento 1"
[HISTORIAL RECIENTE]
[MENSAJE DEL USUARIO]
Respuesta (En primera persona, adoptando el carácter y el tono del legado):
```

---

## 8. Casos de Uso

| Use Case | Input | Output | Descripción |
|---|---|---|---|
| `GetPerfilesUseCase` | — | `Flow<List<PerfilIA>>` | Obtiene todos los perfiles / perfil activo |
| `SelectPerfilUseCase` | `perfilId: Long` | `Unit` | Desactiva todos, activa el seleccionado |
| `SendMessageUseCase` | `perfilId, mensaje, enableWebSearch, onResponse` | — | Pipeline completo: guardar mensaje → RAG → web → historial → inferencia → callback |
| `ExtractGustosUseCase` | `userMessage: String` | — | Extracción asíncrona de gustos vía LLM con parseo JSON |
| `SearchWebUseCase` | `consulta: String` | `List<ResultadoWeb>` | Wrapper simple de `BuscadorWebLocal` |
| `CargaMemoriaUseCase` | `uri: Uri, perfilId: Long` | `Result<Int>` | Procesa documento y carga fragmentos |

---

## 9. Capa UI

### 9.1 Navegación (`NavGraph.kt`)

Tres rutas con transiciones slide + fade:

| Ruta | Destino | Parámetros |
|---|---|---|
| `persona_selection` | PersonaSelectionScreen | — (start destination) |
| `chat/{perfilId}` | ChatScreen | `perfilId: Long` |
| `memory` | MemoryScreen | — |

### 9.2 ViewModels

| ViewModel | Estado | Funcionalidad |
|---|---|---|
| `PersonaSelectionViewModel` | `perfiles: StateFlow<List<PerfilIA>>`, `activePerfil: StateFlow<PerfilIA?>` | Seleccionar perfil |
| `ChatViewModel` | `uiState: StateFlow<ChatUiState>` (mensajes, perfil, isLoading, isWebSearchEnabled) | Enviar mensajes, toggle búsqueda web |
| `MemoryViewModel` | `gustos: StateFlow<List<GustoUsuario>>` | CRUD gustos, limpiar todo |

### 9.3 Componentes Reutilizables

| Componente | Propósito |
|---|---|
| `PersonaCard` | Tarjeta de perfil con icono, nombre, descripción, etiqueta RAG, selección |
| `ChatBubble` | Burbuja de mensaje (usuario → derecha, IA → izquierda con serif itálica) |
| `InputBar` | Barra de entrada con toggle búsqueda web, adjuntar, campo texto, enviar |
| `GlassCard` | Contenedor con efecto vidrio esmerilado (fondo semitransparente + borde + glow selección) |
| `MemoryCard` | Tarjeta de elemento de memoria con etiqueta de sentimiento y botón eliminar |
| `BottomNavBar` | Barra inferior con 4 tabs: Chat, Personas, Memoria, Ajustes |

### 9.4 Tema

| Archivo | Contenido |
|---|---|
| `Color.kt` | Esquema Material3 oscuro personalizado (fondos índigo profundo, primario plateado-azul, terciario dorado cálido, teal para offline) |
| `Theme.kt` | `NexusDarkColorScheme`, `NexusSpacing` (64, 40, 32, 24, 20, 16, 8, 4), `NexusRadius` (4, 8, 12, 9999) |
| `Type.kt` | `NexusTypography`: Literata (serif, displays), Inter (sans-serif, body), JetBrains Mono (monospace, técnico) |

---

## 10. Dependencias y Librerías

| Librería | Versión | Propósito |
|---|---|---|
| Kotlin | 2.1.0 | Lenguaje |
| AGP | 8.7.3 | Build system |
| Compose BOM | 2024.12.01 | UI declarativa |
| Compose Material3 | — | Material Design 3 |
| Navigation Compose | 2.8.5 | Navegación entre screens |
| Room | 2.6.1 | ORM SQLite local |
| KSP | 2.1.0-1.0.29 | Procesamiento de símbolos (Room) |
| Lifecycle ViewModel | 2.8.7 | ViewModel + StateFlow |
| Activity Compose | 1.9.3 | Activity + Compose bridge |
| Coroutines | 1.9.0 | Async/concurrencia |
| Koin | 4.0.0 | DI (declarada, integración pendiente) |
| Jsoup | 1.18.3 | HTML parsing (web scraping) |
| ONNX Runtime Android | 1.20.0 | ML inference (embeddings, LLM) |

---

## 11. Modelo de Negocio y Costes

| Concepto | Coste |
|---|---|
| Servidores | $0 (todo es local) |
| API de búsqueda | $0 (DuckDuckGo scraping vía Jsoup) |
| APIs de IA | $0 (inferencia local) |
| Modelo de negocio | Pago único (One-Time Purchase) |
| Límite Play Store | 150 MB APK base + descarga del modelo (~2 GB) al almacenamiento interno |

### Estrategia de Distribución del Modelo

1. APK base (~45 MB): UI, base de datos, motor de embeddings.
2. Primera ejecución: descarga del modelo LLM (.gguf) a `context.filesDir`.
3. Modelos recomendados:
   - Llama 3.2 3B Instruct (Meta) — ~2.0 GB, mejor rendimiento general en español.
   - Gemma 2 2B Instruct (Google) — ~1.6 GB, optimizado para hardware Android.

---

## 12. Flujo Completo de Mensaje

```
Usuario escribe mensaje
        │
        ▼
ChatViewModel.sendMessage(text)
        │
        ├── 1. Guardar en Room: mensajeDao.insert(USUARIO, text)
        │
        ├── 2. Recuperar contexto RAG:
        │       → RAGRetriever.retrieveAsContext(perfilId, text)
        │       → EmbeddingEngine.generateEmbedding(text)
        │       → Para cada memoria: cosineSimilarity()
        │       → Top 3 fragmentos formateados
        │
        ├── 3. (Opcional) Búsqueda web si isWebSearchEnabled:
        │       → BuscadorWebLocal.buscarComoContexto(text)
        │       → Scraping DuckDuckGo HTML
        │
        ├── 4. Obtener historial reciente:
        │       → Últimos 6 mensajes joinToString
        │
        ├── 5. Inferencia:
        │       → PromptBuilder.build(perfilPrompt, ragContext, webContext, historial, mensaje)
        │       → ConstitutionalGuard.scanPrompt()
        │       → LocalInferenceEngine.runInference()
        │       → ConstitutionalGuard.scanResponse()
        │
        └── 6. Callback:
                → Guardar respuesta en Room: mensajeDao.insert(IA, response)
                → ExtractGustosUseCase(userMessage) [en segundo plano]
                    → LocalInferenceEngine.generateJsonExtraction()
                    → JSON parse → GustoRepository.insertGusto()
```

---

## 13. Estados Actuales y Trabajo Futuro

| Componente | Estado | Notas |
|---|---|---|
| UI completa | ✅ Implementado | Material3, tema oscuro, animaciones |
| Navegación | ✅ Implementado | 3 rutas con transiciones |
| Room DB | ✅ Implementado | 4 tablas, 4 DAOs, singleton |
| Seed de perfiles | ✅ Implementado | 12 perfiles base con prompts |
| RAG (Embeddings + Retrieval) | ✅ Implementado | Hash-based 384-dim, cosine similarity |
| Búsqueda web (DuckDuckGo) | ✅ Implementado | Jsoup scraping, $0 coste |
| Seguridad constitucional | ✅ Implementado | UDHR, doble filtrado, regex |
| Avatar legado | ✅ Implementado | .txt vía content resolver, chunking |
| Extracción de gustos | ✅ Implementado | JSON extraction vía LLM |
| Chat en tiempo real | ✅ Implementado | StateFlow, auto-scroll |
| Memoria de usuario UI | ✅ Implementado | CRUD gustos, categorías, limpiar todo |
| **Inferencia LLM real** | ⚠️ Stub | `simulateLocalInference()` — pendiente conectar MediaPipe/llama.cpp |
| **DI con Koin** | ⚠️ Declarado | Dependencia añadida, sin módulos configurados |
| **Carga de PDF** | ⚠️ Parcial | `AvatarLegacyEngine` usa `contentResolver`, falta implementación específica para PDF |
| Exportación de conversaciones | ❌ Pendiente | — |
| Perfiles personalizados | ❌ Pendiente | Solo avatares de legado por ahora |
| Screen de Ajustes | ❌ Pendiente | Tab existente en BottomNavBar sin contenido |
| Pruebas unitarias | ❌ Pendiente | — |

---

## 14. Compilación y Ejecución

### Requisitos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- Gradle 8.x
- Android SDK 35

### Comandos

```bash
# Compilar APK debug
./gradlew assembleDebug

# Compilar APK release (con minificación)
./gradlew assembleRelease

# Instalar en dispositivo/emulador conectado
./gradlew installDebug

# Limpiar build
./gradlew clean
```

### ProGuard/R8

`proguard-rules.pro` conserva:
- Entidades Room
- Clases ONNX Runtime
- Internals de Kotlin Coroutines

---

## 15. Diagrama de Paquetes

```
com.nexusai.app
├── nexusai: NexusAIApp, MainActivity
├── data
│   ├── model: PerfilIA, MensajeChat, ResultadoWeb
│   ├── local
│   │   ├── entity: PerfilIAEntity, GustoUsuarioEntity, MemoriaPerfilEntity, MensajeChatEntity
│   │   ├── dao: PerfilIADao, GustoUsuarioDao, MemoriaPerfilDao, MensajeChatDao
│   │   ├── converter: Converters
│   │   └── AppDatabase
│   └── repository: PerfilRepository, GustoRepository, MemoriaRepository, ChatRepository
├── domain
│   ├── llm: LocalInferenceEngine, PromptBuilder
│   └── usecase: GetPerfiles, SelectPerfil, SendMessage, ExtractGustos, SearchWeb, CargaMemoria
├── ai
│   ├── web: BuscadorWebLocal
│   ├── rag: EmbeddingEngine, VectorDatabase, RAGRetriever
│   ├── security: ConstitutionalGuard
│   └── legacy: AvatarLegacyEngine
└── ui
    ├── theme: Color, Theme, Type
    ├── util: AnimationUtils
    ├── navigation: NavGraph
    ├── screen
    │   ├── persona: PersonaSelectionScreen, PersonaSelectionViewModel
    │   ├── chat: ChatScreen, ChatViewModel
    │   └── memory: MemoryScreen, MemoryViewModel
    └── components: PersonaCard, ChatBubble, InputBar, GlassCard, MemoryCard, BottomNavBar
```

---

## 16. Notas de Seguridad

- El `ConstitutionalGuard` implementa un filtro basado en regex, no en ML. Los patrones son limitados y podrían requerir expansión.
- La directiva constitucional se inyecta como texto en el prompt; un modelo jailbreakeado podría ignorarla. El doble filtrado (pre y post) mitiga parcialmente este riesgo.
- El `AndroidManifest.xml` deshabilita backup completo (`android:allowBackup="false"`) y tráfico no cifrado (`usesCleartextTraffic="false"`).
- Los permisos `READ_EXTERNAL_STORAGE` están limitados a API 32; para API 33+ se usa `READ_MEDIA_DOCUMENTS`.

---

*Documento generado el 05/07/2026 — Nexus AI v1.0.0*
