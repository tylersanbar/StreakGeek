/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.arbitrarylogic.streakgeek.ui.habit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.arbitrarylogic.streakgeek.data.local.database.Habit
import dev.arbitrarylogic.streakgeek.data.local.database.HabitInstance
import dev.arbitrarylogic.streakgeek.data.preview.getPreviewHabitInstances
import dev.arbitrarylogic.streakgeek.ui.components.TopAppBarWithNavigation
import dev.arbitrarylogic.streakgeek.ui.theme.MyApplicationTheme
import java.time.LocalDate

@Composable
fun HabitScreen(
    modifier: Modifier = Modifier,
    viewModel: HabitViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val habitInstances by viewModel.habitInstancesForDay.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsState()
    HabitScreen(
        habitInstances = habitInstances,
        onAddNewHabit = { navController.navigate("addHabit") },
        selectedDate = selectedDate,
        onPreviousDay = { viewModel.goToPreviousDay() },
        onNextDay = { viewModel.goToNextDay() },
        onToggleHabitCompletion = { habit, instance ->
            viewModel.toggleHabitInstanceCompletion(habit, instance)
        },
        modifier = modifier,
    )
}

@Composable
internal fun HabitScreen(
    habitInstances: List<Pair<Habit, HabitInstance?>>,
    onAddNewHabit: () -> Unit = {},
    onPreviousDay: () -> Unit = {},
    onNextDay: () -> Unit,
    onToggleHabitCompletion: (Habit, HabitInstance?) -> Unit,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate
) {
    Scaffold(
        topBar = {
            TopAppBarWithNavigation(
                currentDay = selectedDate,
                onPreviousDay = onPreviousDay,
                onNextDay = onNextDay
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = onAddNewHabit,
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add, contentDescription = "Add Habit",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(34.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(modifier) {
            HabitList(
                modifier = Modifier.padding(innerPadding),
                habitInstances = habitInstances,
                selectedDate = selectedDate,
                onToggleHabitCompletion = onToggleHabitCompletion
            )
        }
    }
}

@Composable
internal fun HabitList(
    habitInstances: List<Pair<Habit, HabitInstance?>>,
    onToggleHabitCompletion: (Habit, HabitInstance?) -> Unit,
    selectedDate: LocalDate,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(count = habitInstances.size,
            key = { index -> "${habitInstances[index]}_{$selectedDate}" }
        ) { index ->
            val (habit, instance) = habitInstances[index]
            HabitCard(
                habit = habit,
                habitInstance = instance,
                onToggleCompletion = onToggleHabitCompletion
            )
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        HabitScreen(
            onAddNewHabit = {},
            onPreviousDay = {},
            onNextDay = {},
            selectedDate = LocalDate.now(),
            habitInstances = getPreviewHabitInstances(),
            onToggleHabitCompletion = { _, _ -> },
        )
    }
}


@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        HabitScreen(
            onAddNewHabit = {},
            onPreviousDay = {},
            onNextDay = {},
            selectedDate = LocalDate.now(),
            habitInstances = getPreviewHabitInstances(),
            onToggleHabitCompletion = { _, _ -> },
        )
    }
}
