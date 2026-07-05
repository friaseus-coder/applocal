package com.nexusai.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nexusai.app.ui.theme.OutlineVariant
import com.nexusai.app.ui.theme.Primary

private val glassBackground = Color(0xFF1E293B).copy(alpha = 0.4f)
private val selectedGlassBackground = Color(0xFFBEC6E0).copy(alpha = 0.1f)
private val glassBorder = Color(0xFF334155).copy(alpha = 0.5f)
private val selectedGlow = Color(0xFFBEC6E0).copy(alpha = 0.15f)

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    borderColor: Color = if (isSelected) Primary.copy(alpha = 1f) else glassBorder,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                color = if (isSelected) selectedGlassBackground else glassBackground,
                shape = shape
            )
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 1.5.dp,
                        color = borderColor,
                        shape = shape
                    )
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = shape
                    )
                }
            )
            .then(
                if (isSelected) {
                    Modifier.background(
                        brush = Brush.radialGradient(
                            colors = listOf(selectedGlow, Color.Transparent),
                            radius = 200f
                        ),
                        shape = shape
                    )
                } else {
                    Modifier
                }
            )
            .padding(16.dp)
    ) {
        content()
    }
}
