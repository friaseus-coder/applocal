package com.nexusai.app.ui.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexusai.app.NexusAIApp
import com.nexusai.app.ai.rag.EmbeddingEngine
import com.nexusai.app.ai.rag.RAGRetriever
import com.nexusai.app.ai.security.ConstitutionalGuard
import com.nexusai.app.ai.web.BuscadorWebLocal
import com.nexusai.app.data.repository.ChatRepository
import com.nexusai.app.data.repository.GustoRepository
import com.nexusai.app.data.repository.PerfilRepository
import com.nexusai.app.domain.llm.LocalInferenceEngine
import com.nexusai.app.domain.usecase.ExtractGustosUseCase
import com.nexusai.app.ui.components.ChatBubble
import com.nexusai.app.ui.components.InputBar
import com.nexusai.app.ui.components.YouTubeCard
import com.nexusai.app.ui.theme.Background
import com.nexusai.app.ui.theme.OnSurfaceVariant
import com.nexusai.app.ui.theme.OutlineVariant
import com.nexusai.app.ui.theme.Primary
import com.nexusai.app.ui.theme.PrimaryContainer
import com.nexusai.app.ui.theme.SurfaceContainer
import com.nexusai.app.ui.theme.Tertiary
import com.nexusai.app.ui.theme.teal

@Composable
fun ChatScreen(
    perfilId: Long,
    onNavigateToMemory: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val app = NexusAIApp.instance
    val inferenceEngine = remember { LocalInferenceEngine(app) }
    val embeddingEngine = remember { EmbeddingEngine(app) }
    val ragRetriever = remember { RAGRetriever(app, app.memoriaDao, embeddingEngine) }
    val buscadorWeb = remember { BuscadorWebLocal() }
    val guard = remember { ConstitutionalGuard() }

    val viewModel: ChatViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = ChatViewModel.Factory(
            perfilId = perfilId,
            perfilRepository = PerfilRepository(app.perfilDao),
            chatRepository = ChatRepository(app.mensajeDao),
            inferenceEngine = inferenceEngine,
            ragRetriever = ragRetriever,
            buscadorWeb = buscadorWeb,
            extractGustosUseCase = ExtractGustosUseCase(
                GustoRepository(app.gustoDao),
                inferenceEngine
            ),
            guard = guard
        )
    )

    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    LaunchedEffect(uiState.mensajes.size) {
        if (uiState.mensajes.isNotEmpty()) {
            listState.animateScrollToItem(uiState.mensajes.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Background.copy(alpha = 0.7f))
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Primary
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = PrimaryContainer,
                                shape = RoundedCornerShape(50)
                            )
                            .border(
                                width = 1.dp,
                                color = Primary.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = uiState.perfil?.nombre ?: "Cargando...",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            lineHeight = 28.sp,
                            color = Primary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(color = teal, shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Local Engine Active",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = Tertiary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = SurfaceContainer,
                                shape = RoundedCornerShape(50)
                            )
                            .border(
                                width = 1.dp,
                                color = OutlineVariant.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = onNavigateToMemory) {
                        Icon(
                            Icons.Default.Psychology,
                            contentDescription = "Memoria",
                            tint = OnSurfaceVariant
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = OnSurfaceVariant
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = SurfaceContainer.copy(alpha = 0.8f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, OutlineVariant.copy(alpha = 0.2f)
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            text = "Hoy",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }

            if (uiState.mensajes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Inicia una conversación con ${uiState.perfil?.nombre ?: "tu compañero"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }

            items(uiState.mensajes, key = { it.id }) { mensaje ->
                ChatBubble(
                    mensaje = mensaje,
                    personaName = uiState.perfil?.nombre ?: "Nexus AI",
                    isLastAi = mensaje.remitente == "IA" &&
                            uiState.mensajes.lastOrNull()?.id == mensaje.id
                )
                val videos = uiState.videosYouTube[mensaje.id]
                if (videos != null) {
                    videos.forEach { video ->
                        YouTubeCard(
                            video = video,
                            modifier = Modifier.padding(start = 40.dp)
                        )
                    }
                }
            }

            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Primary,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }

        InputBar(
            value = inputText,
            onValueChange = { inputText = it },
            onSend = {
                viewModel.sendMessage(inputText)
                inputText = ""
            },
            isWebSearchEnabled = uiState.isWebSearchEnabled,
            onToggleWebSearch = { viewModel.toggleWebSearch() }
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}
