package dev.arbitrarylogic.streakgeek.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

fun Modifier.pressAndHoldGesture(
    fillProgress: Animatable<Float, *>,
    isCompleted: Boolean,
    durationMillis: Int,
    onComplete: () -> Unit,
    onCancel: () -> Unit
): Modifier = pointerInput(isCompleted) {
    detectTapGestures(
        onPress = {
            coroutineScope {
                val targetValue = if (isCompleted) 0f else 1f
                val animationJob = launch(Dispatchers.Default) {
                    fillProgress.animateTo(
                        targetValue = targetValue,
                        animationSpec = tween(
                            durationMillis = durationMillis,
                            easing = FastOutLinearInEasing
                        ),
                    )
                    onComplete()
                }
                if (tryAwaitRelease()) {
                    // User released early: cancel the animation and reverse progress
                    animationJob.cancel()
                    val elapsedTime = (fillProgress.value * durationMillis).toInt()
                    launch {
                        fillProgress.animateTo(
                            targetValue = if (isCompleted) 1f else 0f,
                            animationSpec = tween(elapsedTime)
                        )
                    }
                    onCancel()
                }
            }
        }
    )
}


