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

package dev.arbitrarylogic.streakgeek.data.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.arbitrarylogic.streakgeek.data.HabitRepository
import dev.arbitrarylogic.streakgeek.data.local.database.Habit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

//    @Singleton
//    @Binds
//    fun bindsHabitRepository(
//        habitRepository: DefaultHabitRepository
//    ): HabitRepository
}

class FakeHabitRepository @Inject constructor() : HabitRepository {
    override val habits: Flow<List<Habit>> = flowOf(fakeHabits)

    override suspend fun add(name: String, description: String) {
        throw NotImplementedError()
    }
}

val fakeHabits = listOf(Habit(0, "One"), Habit(1, "Two"), Habit(2, "Three"))
