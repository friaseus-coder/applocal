package com.nexusai.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexusai.app.data.model.PerfilIA
import com.nexusai.app.ui.theme.OnSecondaryContainer
import com.nexusai.app.ui.theme.OnSurface
import com.nexusai.app.ui.theme.OnSurfaceVariant
import com.nexusai.app.ui.theme.Primary
import com.nexusai.app.ui.theme.SecondaryContainer

@Composable
fun PersonaCard(
    perfil: PerfilIA,
    isSelected: Boolean,
    onSelect: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect(perfil.id) },
        isSelected = isSelected
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 0.dp, end = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Seleccionado",
                        tint = Primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconoTipo(perfil.tipo),
                            contentDescription = null,
                            tint = if (isSelected) Primary else OnSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = perfil.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = perfil.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = SecondaryContainer
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        text = "Local RAG: ${perfil.tagsRag}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = OnSecondaryContainer,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private fun iconoTipo(tipo: String): ImageVector {
    return when (tipo) {
        "AMIGO" -> Icons.Default.Favorite
        "MENTOR" -> Icons.Default.School
        "ESTOICO" -> Icons.Default.Shield
        "MISTICO" -> Icons.Default.AutoAwesome
        "ARQUITECTO" -> Icons.Default.Architecture
        "CREATIVO" -> Icons.Default.Palette
        "GUARDIAN" -> Icons.Default.Lock
        "ORACULO" -> Icons.Default.Visibility
        "SABIO" -> Icons.Default.SelfImprovement
        "DESAFIANTE" -> Icons.Default.Bolt
        "BIBLIOTECARIO" -> Icons.Default.MenuBook
        "COMEDIANTE" -> Icons.Default.Mood
        else -> Icons.Default.Favorite
    }
}
