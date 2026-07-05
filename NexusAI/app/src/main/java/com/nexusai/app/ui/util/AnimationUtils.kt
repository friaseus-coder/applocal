package com.nexusai.app.ui.util

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically

object AnimationUtils {
    val fadeSlideIn = fadeIn(animationSpec = tween(500)) +
            slideInVertically(
                animationSpec = tween(500),
                initialOffsetY = { it / 4 }
            )

    val staggerDelay = 100L
}
