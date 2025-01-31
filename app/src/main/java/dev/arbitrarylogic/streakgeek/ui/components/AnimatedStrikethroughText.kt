package dev.arbitrarylogic.streakgeek.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

@Composable
fun AnimatedStrikethroughText(
    text: String,
    progress: Float
) {
    Text(
        text = text,
        modifier = Modifier.drawBehind(drawStrikethroughLine(progress)),
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = MaterialTheme.typography.displayMedium.fontSize,
    )
}

@Composable
private fun drawStrikethroughLine(progress: Float): DrawScope.() -> Unit =
    {
        drawLine(
            color = Color.Black,
            start = Offset(0f, 84f),
            end = Offset((size.width * progress).coerceAtMost(size.width), 84f),
            strokeWidth = 12f,
            alpha = 0.7f
        )
    }