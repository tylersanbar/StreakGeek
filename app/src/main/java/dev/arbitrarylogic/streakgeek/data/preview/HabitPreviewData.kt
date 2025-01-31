package dev.arbitrarylogic.streakgeek.data.preview

import dev.arbitrarylogic.streakgeek.data.local.database.Habit
import dev.arbitrarylogic.streakgeek.data.local.database.HabitInstance
import java.time.LocalDate
import java.time.LocalTime

fun getPreviewHabitInstances(): List<Pair<Habit, HabitInstance?>> {
    return listOf(
        Pair(
            Habit(0, "Read Book", "Read a book"), HabitInstance(
                habitId = 0,
                date = LocalDate.now(),
                isCompleted = true,
                notes = "test notes",
                completionTime = LocalTime.now()
            )
        ),
        Pair(
            Habit(1, "Finish App", "Finish the app"), HabitInstance(
                habitId = 1,
                date = LocalDate.now(),
                isCompleted = false,
                notes = "test notes",
                completionTime = LocalTime.now()
            )
        ),
    )
}