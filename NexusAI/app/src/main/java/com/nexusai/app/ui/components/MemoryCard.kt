package com.nexusai.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nexusai.app.data.repository.GustoUsuario
import com.nexusai.app.ui.theme.Error
import com.nexusai.app.ui.theme.OnSurface
import com.nexusai.app.ui.theme.OnSurfaceVariant
import com.nexusai.app.ui.theme.TertiaryFixed

@Composable
fun MemoryCard(
    gusto: GustoUsuario,
    onDelete: (GustoUsuario) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
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
                    style = MaterialTheme.typography.labelSmall,
                    color = labelColor
                )
            }

            IconButton(onClick = { onDelete(gusto) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = OnSurfaceVariant
                )
            }
        }
    }
}
