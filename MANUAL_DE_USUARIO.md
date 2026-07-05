# Manual de Usuario — Nexus AI

**Versión:** 1.0.0  
**Plataforma:** Android 8.0+ (API 26)  
**Idioma:** Español  

---

## 1. Introducción

Nexus AI es un asistente personal de inteligencia artificial que funciona **completamente offline** en tu dispositivo Android. Todos los datos, conversaciones y preferencias se almacenan de forma local y nunca salen de tu teléfono. No requiere conexión a internet para las funcionalidades principales.

### Conceptos Clave

| Concepto | Descripción |
|---|---|
| **Perfil IA** | Personalidad o arquetipo con el que interactúas (12 perfiles predefinidos). |
| **Inferencia Local** | El modelo de IA se ejecuta en tu teléfono, no en servidores externos. |
| **RAG (Búsqueda Semántica)** | El asistente busca información relevante en su base de conocimiento local para darte respuestas más precisas. |
| **Memoria de Usuario** | Preferencias, gustos e intereses que el asistente aprende de ti automáticamente. |
| **Avatar de Legado** | Perfil personalizado creado a partir de documentos (diarios, memorias, libros) que cargues. |
| **Búsqueda Web Local** | Búsqueda en internet anonimizada a través de DuckDuckGo, sin costo y sin compartir tus datos. |

---

## 2. Primeros Pasos

### Instalación

1. Descarga Nexus AI desde Google Play Store.
2. Al abrir la app por primera vez, se inicializarán las bases de datos locales y los 12 perfiles base.
3. El modelo de lenguaje (LLM) se descargará directamente al almacenamiento interno de la app (~1.6–2.0 GB).
4. Una vez completada la descarga, la app está lista para usarse sin conexión.

### Permisos

| Permiso | Propósito |
|---|---|
| `INTERNET` | Búsqueda web opcional (DuckDuckGo) y descarga inicial del modelo. |
| `ACCESS_NETWORK_STATE` | Detectar disponibilidad de red. |
| `READ_EXTERNAL_STORAGE` / `READ_MEDIA_DOCUMENTS` | Cargar documentos para avatares de legado. |

> Todos los permisos son opcionales para la funcionalidad principal. Sin `INTERNET` la app funciona completamente offline.

---

## 3. Pantalla de Selección de Compañero

Al abrir la aplicación, verás una cuadrícula con **12 tarjetas de personalidad**. Cada tarjeta muestra:

- **Icono** representativo del arquetipo.
- **Nombre** del perfil.
- **Descripción** breve de su propósito.
- **Etiqueta RAG** que indica su área de conocimiento local.

### Lista de Perfiles Base

| # | Perfil | Arquetipo | Área RAG |
|---|---|---|---|
| 1 | El Mejor Amigo | Apoyo emocional, escucha activa | Psychology |
| 2 | El Mentor | Productividad, crecimiento profesional | Business |
| 3 | El Estoico | Razonamiento lógico, fortaleza mental | Philosophy |
| 4 | El Místico | Espiritualidad, trascendencia | Bible / Coran |
| 5 | El Arquitecto | Diseño de sistemas, pensamiento estructurado | Engineering |
| 6 | El Creativo | Innovación, lluvia de ideas | Arts |
| 7 | El Guardián | Seguridad y privacidad digital | Sys-Encryption |
| 8 | El Oráculo | Predicciones basadas en datos | Data Science |
| 9 | El Sabio | Sabiduría atemporal, equilibrio | Ancient Texts |
| 10 | El Desafiante | Pensamiento crítico, debate | Debate |
| 11 | El Bibliotecario | Organización de documentos | Documentation |
| 12 | El Comediante | Humor inteligente | Pop Culture |

### Cómo Seleccionar un Perfil

1. Toca la tarjeta del perfil que desees. Se iluminará con un borde brillante y aparecerá un ícono de verificación.
2. Presiona el botón **"Continuar con mi Compañero"** que aparece en la parte inferior.
3. Accederás a la pantalla de chat con ese perfil.

---

## 4. Pantalla de Chat

### Elementos de la Interfaz

```
┌─────────────────────────────────────────┐
│  ←  [Avatar]  Nombre del Perfil        │
│               ● Local Engine Active  🔒 │
│                                    🧠 🔍│
├─────────────────────────────────────────┤
│                                         │
│  ┌──────────────────────────┐           │
│  │ Mensaje del usuario      │  ← Burbuja│
│  └──────────────────────────┘   usuario │
│                                         │
│           ┌──────────────────────┐       │
│           │ Respuesta de la IA   │ ← Serif│
│           │ (estilo itálico)     │   itálica│
│           └──────────────────────┘       │
│  📖 Knowledge Source: Documentos locales │
│  14:30 • Local Inferencing              │
│                                         │
│         ↻  (indicador de carga)         │
├─────────────────────────────────────────┤
│  🔗 Local Web Search [═●═]             │
│  📎 ┌──────────────────┐ ➤             │
│     │ Escribe tu...    │               │
│     └──────────────────┘               │
└─────────────────────────────────────────┘
```

### Barra Superior

| Elemento | Descripción |
|---|---|
| **← (Volver)** | Regresa a la pantalla de selección de perfiles. |
| **Avatar + Nombre** | Identifica el perfil activo. |
| **● Local Engine Active** | Indicador verde: el motor de IA local está operativo. |
| **🔒 (Seguridad)** | Acceso directo a información de seguridad. |
| **🧠 (Memoria)** | Abre la pantalla de gestión de memoria del usuario. |
| **🔍 (Buscar)** | Botón para búsqueda (funcionalidad futura). |

### Barra Inferior (Entrada de Texto)

| Elemento | Descripción |
|---|---|
| **🔗 Local Web Search** | Interruptor para activar búsqueda web opcional. Al activarlo, la IA podrá consultar DuckDuckGo para responder preguntas sobre información actual. |
| **📎 (Adjuntar)** | Permite seleccionar documentos para cargar como memoria de legado. |
| **Campo de texto** | Escribe tu mensaje (placeholder: "Escribe tu reflexión..."). |
| **➤ (Enviar)** | Botón dorado para enviar el mensaje. |

### Burbujas de Chat

- **Mensajes del usuario**: Burbuja alineada a la derecha, fondo azul primario.
- **Respuestas de la IA**: Burbuja alineada a la izquierda, fondo semitransparente oscuro, texto en serif itálica de 20sp para distinguir visualmente la sabiduría del asistente.
- **Fuente de conocimiento**: Debajo de cada respuesta de la IA se muestra "Knowledge Source: Documentos locales" si se usó RAG.
- **Marca de tiempo**: Cada mensaje de la IA muestra la hora y la etiqueta "Local Inferencing".

---

## 5. Búsqueda Web Local

Nexus AI puede buscar información actualizada en internet sin comprometer tu privacidad.

### Cómo Funciona

1. Activa el interruptor **"Local Web Search"** en la barra inferior.
2. Escribe tu pregunta normalmente.
3. La app enviará la consulta a DuckDuckGo de forma anónima (sin cookies, sin sesión, sin identificar tu IP).
4. Los resultados se inyectarán como contexto en el prompt de la IA.
5. La IA generará una respuesta combinando su conocimiento interno con la información web obtenida.

> **Privacidad**: La búsqueda se realiza a través de DuckDuckGo HTML, que no rastrea a los usuarios. No se envía ningún identificador personal.

---

## 6. Gestión de Memoria del Usuario

Nexus AI **aprende activamente** de tus conversaciones. Analiza en segundo plano tus mensajes para identificar gustos, intereses, valores y disgustos.

### Pantalla "Memoria Local del Nexo"

Esta pantalla (accesible desde el ícono 🧠 en el chat) muestra todo lo que la IA ha aprendido sobre ti.

```
┌─────────────────────────────────────────┐
│  ← Memoria Local del Nexo              │
│    Soberanía de Datos       🔒 Offline  │
├─────────────────────────────────────────┤
│ Estos datos nunca salen de tu teléfono. │
│                                         │
│ ♥ GUSTOS                               │
│ ┌─────────────────────────────────┐     │
│ │ Café espresso                   │     │
│ │ [ GUSTA ]                  🗑   │     │
│ └─────────────────────────────────┘     │
│                                         │
│ ⚖ VALORES MORALES                      │
│ ┌─────────────────────────────────┐     │
│ │ Honestidad                      │     │
│ │ [ GUSTA ]                  🗑   │     │
│ └─────────────────────────────────┘     │
│                                         │
│ ⚡ ESTRESORES                           │
│ ┌─────────────────────────────────┐     │
│ │ Despertarse temprano            │     │
│ │ [ NO GUSTA ]               🗑   │     │
│ └─────────────────────────────────┘     │
│                                         │
│ ┌─────────────────────────────────┐     │
│ │ 🔴 Limpiar Memoria Completa    │     │
│ └─────────────────────────────────┘     │
└─────────────────────────────────────────┘
```

### Categorías de Memoria

| Categoría | Icono | Descripción |
|---|---|---|
| Gustos | ♥ | Cosas que le gustan al usuario (comida, música, hobbies, etc.) |
| Valores Morales | ⚖ | Principios éticos y valores detectados |
| Estresores | ⚡ | Situaciones o cosas que no le gustan |

### Acciones Disponibles

- **Eliminar un elemento individual**: Toca el ícono 🗑 en la tarjeta correspondiente.
- **Limpiar toda la memoria**: Presiona el botón "Limpiar Memoria Completa" y confirma en el diálogo de advertencia.

> Todos los datos almacenados en la memoria **nunca salen de tu teléfono**. Están guardados en la base de datos local Room de la aplicación.

---

## 7. Avatares de Legado (Documentos Personales)

Puedes crear un perfil personalizado a partir de documentos de texto o PDF. Esta función te permite conversar con una simulación basada en:

- Diarios personales de un ser querido.
- Memorias o autobiografías.
- Libros de personajes históricos.
- Cartas o escritos.

### Cómo Crear un Avatar de Legado

1. En la pantalla de chat, toca el ícono **📎 (Adjuntar)** en la barra de entrada.
2. Selecciona un archivo `.txt` o `.pdf` del almacenamiento de tu dispositivo.
3. La app procesará el documento:
   - Lee el texto completo.
   - Divide en fragmentos de ~300 caracteres.
   - Genera vectores semánticos (embeddings) para cada fragmento.
   - Los almacena en la base de datos local vinculados a un nuevo perfil.
4. Una vez procesado, puedes interactuar con el avatar que hereda el temperamento, opiniones y estilo de escritura del documento.

### Comportamiento del Avatar de Legado

- Responde en **primera persona** simulando ser la persona autora del documento.
- Sus respuestas se basan estrictamente en el contenido del documento.
- Si no encuentra información concreta, puede deducir respuestas creativas que coincidan con el carácter del autor.

---

## 8. Constitucional AI: Sistema de Seguridad

Nexus AI incorpora un **sistema ético inquebrantable** basado en la Declaración Universal de los Derechos Humanos (DUDH).

### Principios de Seguridad

- **Respeto absoluto a la dignidad humana**: No se toleran discursos de odio, discriminación o violencia.
- **Coexistencia y libertad de pensamiento**: La IA promueve puentes de comprensión mutua y empatía.
- **Neutralidad inquebrantable**: Si se intenta manipular a la IA para generar contenido dañino, se negará cortésmente.

### Cómo se Aplica

1. **Antes de generar una respuesta**: El filtro de seguridad escanea el prompt del usuario.
2. **Después de generar la respuesta**: El filtro escanea la respuesta antes de mostrarla.
3. **Directiva constitucional**: Se inyecta automáticamente en cada prompt como instrucción de sistema, por encima de la personalidad del perfil.

> Este sistema previene jailbreaks y usos malintencionados, garantizando que el asistente sea siempre una herramienta pacífica y de crecimiento humano.

---

## 9. Privacidad y Soberanía de Datos

### Qué Datos se Almacenan Localmente

| Tipo de Dato | Ubicación |
|---|---|
| Conversaciones completas | Room DB (`historial_mensajes`) |
| Gustos y preferencias | Room DB (`gustos_usuario`) |
| Fragmentos RAG por perfil | Room DB (`memorias_perfil`) |
| Perfiles y configuraciones | Room DB (`perfiles_ia`) |
| Vectores semánticos | Room DB (serializados en `memorias_perfil`) |
| Modelo de lenguaje (LLM) | Almacenamiento interno de la app |
| Documentos cargados | Solo se procesan en memoria; no se conservan copias |

### Qué NO Sale del Dispositivo

- ❌ Tus mensajes y conversaciones.
- ❌ Tus preferencias, gustos y datos personales.
- ❌ Los documentos que cargues para avatares de legado.
- ❌ Los vectores semánticos de tu base de conocimiento.

### Búsqueda Web y Privacidad

La búsqueda web opcional se realiza a través de **DuckDuckGo HTML** que:
- No utiliza cookies de seguimiento.
- No almacena el historial de búsqueda.
- No vincula las búsquedas a tu identidad.
- La consulta se envía sin datos del usuario ni identificadores del dispositivo.

---

## 10. Preguntas Frecuentes (FAQ)

### ¿Necesito conexión a internet?

No. Nexus AI funciona completamente offline. La conexión solo es necesaria para:
- Descargar el modelo de lenguaje la primera vez.
- Usar la búsqueda web opcional (puedes desactivarla).

### ¿Cuánto espacio ocupa la aplicación?

| Componente | Tamaño Aproximado |
|---|---|
| APK base (interfaz + motor embeddings) | ~45 MB |
| Modelo de lenguaje (Llama 3.2 3B / Gemma 2 2B) | ~1.6–2.0 GB |
| Base de datos (conversaciones, memorias) | Variable según uso |

### ¿Se pueden exportar las conversaciones? (Funcionalidad futura)

Actualmente no hay exportación implementada. Todas las conversaciones permanecen en la base de datos local de la aplicación.

### ¿Cómo restablecer un perfil a su estado original?

Actualmente no hay una función de restablecimiento. Puedes eliminar los datos de la aplicación desde Ajustes del sistema > Aplicaciones > Nexus AI > Almacenamiento > Eliminar datos.

### ¿Puedo cambiar de perfil sin perder el chat?

Al cambiar de perfil, cada perfil mantiene su propio historial de mensajes independiente. Puedes volver al perfil anterior y el historial seguirá ahí.

### ¿Puedo crear mi propio perfil personalizado? (Próximamente)

La función de avatares de legado te permite crear perfiles a partir de documentos. En el futuro se añadirá la opción de crear perfiles desde cero con prompt personalizado.

---

## 11. Solución de Problemas

### La app no responde después de enviar un mensaje

1. Espera unos segundos (el modelo local puede tardar en procesar).
2. Si persiste, cierra y vuelve a abrir la aplicación.
3. Verifica que el modelo de lenguaje se haya descargado correctamente.

### No aparece ningún perfil

1. Asegúrate de haber abierto la app al menos una vez después de la instalación.
2. Si el problema persiste, reinstala la aplicación.

### La búsqueda web no devuelve resultados

1. Verifica que el interruptor "Local Web Search" esté activado.
2. Comprueba que el dispositivo tenga conexión a internet.
3. Algunas consultas muy específicas pueden no encontrar resultados en DuckDuckGo.

### El avatar de legado no se comporta como esperaba

1. Asegúrate de que el documento cargado tenga suficiente texto (mínimo varios párrafos).
2. El avatar se basa exclusivamente en el contenido del documento; si el documento es breve, sus respuestas serán limitadas.
3. Prueba con un documento más extenso y representativo de la personalidad deseada.

---

## 12. Contacto y Soporte

- **Reportar errores**: https://github.com/anomalyco/opencode/issues
- **Correo electrónico**: [pendiente de configuración]

---

*Documento generado el 05/07/2026 — Nexus AI v1.0.0*
