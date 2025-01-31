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

package dev.arbitrarylogic.streakgeek.data

import dev.arbitrarylogic.streakgeek.data.local.database.Habit
import dev.arbitrarylogic.streakgeek.data.local.database.HabitDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [DefaultHabitRepository].
 */
class DefaultHabitRepositoryTest {

    @Test
    fun tasks_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultHabitRepository(FakeHabitDao())

        repository.add("Repository")

        assertEquals(repository.habits.first().size, 1)
    }

}

private class FakeHabitDao : HabitDao {

    private val data = mutableListOf<Habit>()

    override fun getHabits(): Flow<List<Habit>> = flow {
        emit(data)
    }

    override suspend fun insertHabit(item: Habit) {
        data.add(0, item)
    }
}
