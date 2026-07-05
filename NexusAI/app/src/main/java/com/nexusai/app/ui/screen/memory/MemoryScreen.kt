package com.nexusai.app.ui.screen.memory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexusai.app.NexusAIApp
import com.nexusai.app.data.repository.GustoRepository
import com.nexusai.app.data.repository.GustoUsuario
import com.nexusai.app.ui.components.GlassCard
import com.nexusai.app.ui.theme.Background
import com.nexusai.app.ui.theme.Error
import com.nexusai.app.ui.theme.OnSurface
import com.nexusai.app.ui.theme.OnSurfaceVariant
import com.nexusai.app.ui.theme.OutlineVariant
import com.nexusai.app.ui.theme.Primary
import com.nexusai.app.ui.theme.SurfaceContainer
import com.nexusai.app.ui.theme.TertiaryFixed
import com.nexusai.app.ui.theme.teal

@Composable
fun MemoryScreen(
    onNavigateBack: () -> Unit
) {
    val repository = GustoRepository(NexusAIApp.instance.gustoDao)
    val viewModel: MemoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = MemoryViewModel.Factory(repository)
    )

    val gustos by viewModel.gustos.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Limpiar Memoria Completa") },
            text = { Text("¿Estás seguro? Esta acción eliminará toda la información que Nexus AI ha aprendido sobre ti.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAllMemory()
                    showClearDialog = false
                }) {
                    Text("Confirmar", color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Background.copy(alpha = 0.7f))
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
            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Memoria Local del Nexo",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    color = Primary
                )
                Text(
                    text = "Soberanía de Datos",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = TertiaryFixed
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
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
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = teal,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Offline Engine",
                    fontSize = 11.sp,
                    color = teal,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Text(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            text = "Estos datos nunca salen de tu teléfono. Lo que Nexus aprende de ti se guarda en una tabla privada (GustoUsuario) para personalizar tu experiencia local. Puedes borrarlos en cualquier momento.",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val gustosCategorizados = gustos.groupBy { it.categoria }

            for ((categoria, items) in gustosCategorizados) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = iconoCategoria(categoria),
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = categoria,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Primary,
                            letterSpacing = 0.05.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(items, key = { it.id }) { gusto ->
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = gusto.elemento,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                val label = when (gusto.sentimiento) {
                                    "GUSTA" -> "GUSTA"
                                    "NO_GUSTA" -> "NO GUSTA"
                                    else -> gusto.sentimiento
                                }
                                val labelColor = when (gusto.sentimiento) {
                                    "GUSTA" -> TertiaryFixed
                                    "NO_GUSTA" -> Error
                                    else -> OnSurfaceVariant
                                }
                                Text(
                                    text = label,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = labelColor,
                                    modifier = Modifier
                                        .border(
                                            1.dp,
                                            labelColor.copy(alpha = 0.3f),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            IconButton(onClick = { viewModel.deleteGusto(gusto) }) {
                                Icon(
                                    imageVector = Icons.Default.DeleteForever,
                                    contentDescription = "Eliminar",
                                    tint = OnSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            if (gustos.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier.padding(32.dp),
                        text = "Aún no hay información almacenada. Conversa con tu compañero para que comience a conocerte.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showClearDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Error
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Error)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Limpiar Memoria Completa",
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

private fun iconoCategoria(categoria: String): ImageVector {
    return when {
        categoria.contains("GUSTO", ignoreCase = true) || categoria.contains("GUSTA", ignoreCase = true) -> Icons.Default.Favorite
        categoria.contains("VALOR", ignoreCase = true) || categoria.contains("MORAL", ignoreCase = true) -> Icons.Default.Balance
        categoria.contains("ESTRES", ignoreCase = true) -> Icons.Default.Bolt
        else -> Icons.Default.Favorite
    }
}
