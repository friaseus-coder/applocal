package com.nexusai.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexusai.app.ui.theme.OnSurfaceVariant
import com.nexusai.app.ui.theme.OutlineVariant
import com.nexusai.app.ui.theme.Primary
import com.nexusai.app.ui.theme.Tertiary

@Composable
fun InputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isWebSearchEnabled: Boolean,
    onToggleWebSearch: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Escribe tu reflexión..."
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF1E293B).copy(alpha = 0.4f),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = OutlineVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(8.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { onToggleWebSearch() }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Local Web Search",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 28.dp, height = 14.dp)
                                .background(
                                    color = if (isWebSearchEnabled) Primary.copy(alpha = 0.5f)
                                    else OutlineVariant,
                                    shape = CircleShape
                                )
                                .padding(2.dp),
                            contentAlignment = if (isWebSearchEnabled) Alignment.CenterEnd
                            else Alignment.CenterStart
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(
                                        color = if (isWebSearchEnabled)
                                            Color(0xFF131315) else Color(0xFFE4E2E4),
                                        shape = CircleShape
                                    )
                            )
                        }
                    }

                    Text(
                        text = "Llama-3-8B-Private",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = OnSurfaceVariant.copy(alpha = 0.5f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Adjuntar",
                        tint = OnSurfaceVariant,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(24.dp)
                    )

                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        textStyle = TextStyle(
                            color = Color(0xFFE4E2E4),
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        ),
                        cursorBrush = SolidColor(Tertiary),
                        decorationBox = { innerTextField ->
                            Box {
                                if (value.isEmpty()) {
                                    Text(
                                        text = placeholder,
                                        style = TextStyle(
                                            color = OnSurfaceVariant.copy(alpha = 0.4f),
                                            fontSize = 16.sp
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Tertiary,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSend() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Enviar",
                            tint = Color(0xFF3E2D11),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
