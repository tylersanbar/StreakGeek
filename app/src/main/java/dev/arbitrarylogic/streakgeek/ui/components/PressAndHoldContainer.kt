package dev.arbitrarylogic.streakgeek.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

@Composable
fun PressAndHoldContainer(
    modifier: Modifier = Modifier,
    isCompleted: Boolean,
    fillProgress: Animatable<Float, AnimationVector1D> = remember(isCompleted) {
        Animatable(
            if (isCompleted) 1f else 0f
        )
    },
    durationMillis: Int = 1500,
    fillColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    onComplete: () -> Unit = {}, // Called when the press completes
    onCancel: () -> Unit = {}, // Called when the press is canceled
    content: @Composable BoxScope.(progress: Float) -> Unit // Allows content to react to progress
) {
    Box(
        modifier = modifier
            .clip(shape)
            .fillMaxSize()
            .pressAndHoldGesture(
                fillProgress = fillProgress,
                isCompleted = isCompleted,
                durationMillis = durationMillis,
                onComplete = onComplete,
                onCancel = onCancel
            )
            .drawBehind {
                drawRect(
                    size = Size(size.width * fillProgress.value, size.height),
                    brush = SolidColor(fillColor)
                )
            }
    ) {
        content(fillProgress.value)
    }
}
