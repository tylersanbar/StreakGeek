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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arbitrarylogic.streakgeek.data.HabitInstanceRepository
import dev.arbitrarylogic.streakgeek.data.HabitRepository
import dev.arbitrarylogic.streakgeek.data.local.database.Habit
import dev.arbitrarylogic.streakgeek.data.local.database.HabitInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val habitInstanceRepository: HabitInstanceRepository
) : ViewModel() {

    // Manage the current selected date
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val habitInstancesForDay: StateFlow<List<Pair<Habit, HabitInstance?>>> =
        selectedDate.flatMapLatest { day ->
            habitInstanceRepository.getHabitInstancesForDate(day) // Ensure this returns a **Flow** from Room!
        }.combine(habitRepository.habits) { instances, habits ->
            habits.map { habit ->
                val instance = instances.find { it.habitId == habit.uid }
                habit to instance // Pair of Habit and HabitInstance
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Navigate between days
    fun goToPreviousDay() {
        _selectedDate.value = _selectedDate.value.minusDays(1)
    }

    fun goToNextDay() {
        _selectedDate.value = _selectedDate.value.plusDays(1)
    }

    fun addHabit(name: String, description: String) {
        viewModelScope.launch {
            habitRepository.add(name = name, description = description)
        }
    }

    // Toggle completion of a HabitInstance
    fun toggleHabitInstanceCompletion(habit: Habit, instance: HabitInstance?) {
        viewModelScope.launch {
            if (instance != null) {
                habitInstanceRepository.updateHabitInstance(
                    instance.uid,
                    isCompleted = !instance.isCompleted,
                    completionTime = LocalTime.now()
                )
            } else {
                habitInstanceRepository.addHabitInstance(habit.uid, _selectedDate.value)
            }
        }
    }
}

sealed interface HabitUiState {
    object Loading : HabitUiState
    data class Error(val throwable: Throwable) : HabitUiState
    data class Success(val data: List<Habit>) : HabitUiState
}
