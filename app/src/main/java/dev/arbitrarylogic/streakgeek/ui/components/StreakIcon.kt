package dev.arbitrarylogic.streakgeek.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StreakIcon(streakLength: Int, isCompleted: Boolean) {
    Row(modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Check",
            modifier = Modifier
                .padding(8.dp)
                .alpha(if (isCompleted) 1f else 0f)
                .size(50.dp),
        )
        Text(
            text = if (streakLength > 0) streakLength.toString() else "",
            modifier = Modifier.alpha(if (isCompleted) 1f else 0f),
            fontSize = 40.sp
        )
    }
}