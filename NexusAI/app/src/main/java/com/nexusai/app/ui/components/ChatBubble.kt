package com.nexusai.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexusai.app.data.model.MensajeChat
import com.nexusai.app.ui.theme.OnSurface
import com.nexusai.app.ui.theme.OnSurfaceVariant
import com.nexusai.app.ui.theme.OnTertiaryContainer
import com.nexusai.app.ui.theme.OutlineVariant
import com.nexusai.app.ui.theme.Primary
import com.nexusai.app.ui.theme.PrimaryContainer
import com.nexusai.app.ui.theme.Tertiary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatBubble(
    mensaje: MensajeChat,
    personaName: String = "Nexus AI",
    isLastAi: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isUser = mensaje.remitente == "USUARIO"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (isUser) {
            Box(
                modifier = Modifier
                    .background(
                        color = PrimaryContainer,
                        shape = RoundedCornerShape(
                            topStart = 16.dp, topEnd = 16.dp,
                            bottomStart = 16.dp, bottomEnd = 4.dp
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = mensaje.contenido,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurface
                )
            }
        } else {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(50)
                            )
                            .border(
                                width = 1.dp,
                                color = Primary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(50)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = personaName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Semibold,
                        color = Primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF1E293B).copy(alpha = 0.4f),
                            shape = RoundedCornerShape(
                                topStart = 16.dp, topEnd = 16.dp,
                                bottomStart = 4.dp, bottomEnd = 16.dp
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFF334155).copy(alpha = 0.5f),
                            shape = RoundedCornerShape(
                                topStart = 16.dp, topEnd = 16.dp,
                                bottomStart = 4.dp, bottomEnd = 16.dp
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = mensaje.contenido,
                            fontFamily = FontFamily.Serif,
                            fontStyle = FontStyle.Italic,
                            fontSize = 20.sp,
                            lineHeight = 32.sp,
                            color = OnSurface
                        )
                    }
                }

                if (isLastAi) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = Tertiary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Knowledge Source: Documentos locales",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = OnTertiaryContainer,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${formatTime(mensaje.timestamp)} • Local Inferencing",
                    fontSize = 11.sp,
                    color = OnSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 40.dp)
                )
            }
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
