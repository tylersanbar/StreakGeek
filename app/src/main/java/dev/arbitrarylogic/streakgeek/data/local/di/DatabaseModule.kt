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

package dev.arbitrarylogic.streakgeek.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.arbitrarylogic.streakgeek.data.DefaultHabitInstanceRepository
import dev.arbitrarylogic.streakgeek.data.DefaultHabitRepository
import dev.arbitrarylogic.streakgeek.data.HabitInstanceRepository
import dev.arbitrarylogic.streakgeek.data.HabitRepository
import dev.arbitrarylogic.streakgeek.data.local.database.AppDatabase
import dev.arbitrarylogic.streakgeek.data.local.database.HabitDao
import dev.arbitrarylogic.streakgeek.data.local.database.HabitInstanceDao
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideHabitDao(appDatabase: AppDatabase): HabitDao {
        return appDatabase.habitDao()
    }

    @Provides
    fun provideHabitInstanceDao(appDatabase: AppDatabase): HabitInstanceDao {
        return appDatabase.habitInstanceDao()
    }

    @Provides
    @Singleton
    fun provideHabitRepository(habitDao: HabitDao): HabitRepository {
        return DefaultHabitRepository(habitDao)
    }

    @Provides
    @Singleton
    fun provideHabitInstanceRepository(habitInstanceDao: HabitInstanceDao): HabitInstanceRepository {
        return DefaultHabitInstanceRepository(habitInstanceDao)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Habit"
        ).build()
    }
}

