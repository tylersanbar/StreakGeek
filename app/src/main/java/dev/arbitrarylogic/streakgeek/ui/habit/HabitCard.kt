package dev.arbitrarylogic.streakgeek.ui.habit

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.arbitrarylogic.streakgeek.data.local.database.Habit
import dev.arbitrarylogic.streakgeek.data.local.database.HabitInstance
import dev.arbitrarylogic.streakgeek.ui.components.AnimatedStrikethroughText
import dev.arbitrarylogic.streakgeek.ui.components.PressAndHoldContainer
import dev.arbitrarylogic.streakgeek.ui.components.StreakIcon


@Composable
fun HabitCard(
    habit: Habit,
    habitInstance: HabitInstance?,
    onToggleCompletion: (Habit, HabitInstance?) -> Unit
) {
    val isCompleted by rememberUpdatedState(
        habitInstance?.isCompleted ?: false
    )
    val fillProgress = remember(isCompleted) {
        Animatable(
            if (isCompleted) 1f else 0f
        )
    }
    PressAndHoldContainer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(80.dp),
        fillProgress = fillProgress,
        isCompleted = isCompleted,
        durationMillis = 1500, // Adjust duration as needed
        fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
        onComplete = { onToggleCompletion(habit, habitInstance) }
    ) { progress ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            AnimatedStrikethroughText(habit.name, progress)

            //Text(habitInstance?.uid.toString())

            StreakIcon(habitInstance?.streak ?: 0, isCompleted)
        }
    }
}


