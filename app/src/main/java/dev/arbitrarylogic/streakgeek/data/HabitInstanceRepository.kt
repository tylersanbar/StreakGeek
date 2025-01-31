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

import dev.arbitrarylogic.streakgeek.data.local.database.HabitInstance
import dev.arbitrarylogic.streakgeek.data.local.database.HabitInstanceDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

interface HabitInstanceRepository {
    val habitInstances: Flow<List<HabitInstance>> // Optional: Flow of all habit instances

    suspend fun getHabitInstance(habitId: Int, date: LocalDate): HabitInstance?

    suspend fun getHabitInstance(habitInstanceId: Int): HabitInstance?

    suspend fun getHabitInstancesForHabit(habitId: Int): List<HabitInstance>

    suspend fun addHabitInstance(habitId: Int, date: LocalDate)

    suspend fun updateHabitInstance(
        instanceId: Int,
        isCompleted: Boolean,
        completionTime: LocalTime?
    )

    suspend fun deleteHabitInstance(habitInstance: HabitInstance)

    fun getHabitInstancesForDate(date: LocalDate): Flow<List<HabitInstance>>

    suspend fun getLastCompletedInstance(habitId: Int, date: LocalDate): HabitInstance?

    suspend fun updateNextInstance(instance: HabitInstance)
}

class DefaultHabitInstanceRepository @Inject constructor(
    private val habitInstanceDao: HabitInstanceDao
) : HabitInstanceRepository {

    override val habitInstances: Flow<List<HabitInstance>> =
        habitInstanceDao.getAllHabitInstances() // Optional: Get all instances as a Flow

    override suspend fun getHabitInstance(habitInstanceId: Int): HabitInstance? {
        return habitInstanceDao.getHabitInstance(habitInstanceId)
    }

    override suspend fun getHabitInstance(habitId: Int, date: LocalDate): HabitInstance? {
        return habitInstanceDao.getHabitInstance(habitId, date)
    }

    override fun getHabitInstancesForDate(date: LocalDate): Flow<List<HabitInstance>> {
        return habitInstanceDao.getHabitInstancesForDate(date)
    }

    override suspend fun getHabitInstancesForHabit(habitId: Int): List<HabitInstance> {
        return habitInstanceDao.getAllInstancesForHabit(habitId)
    }

    override suspend fun addHabitInstance(habitId: Int, date: LocalDate) {
        val newInstance = HabitInstance(
            habitId = habitId,
            date = date,
            isCompleted = true,
            streak = calculateStreak(habitId, date)
        )
        habitInstanceDao.insertHabitInstance(newInstance)
        updateNextInstance(newInstance)
    }

    override suspend fun updateHabitInstance(
        instanceId: Int,
        isCompleted: Boolean,
        completionTime: LocalTime?
    ) {
        val oldInstance = getHabitInstance(instanceId)
            ?: throw Exception("Habit Instance (id $instanceId) not found")

        val instance = oldInstance.copy(
            isCompleted = isCompleted,
            completionTime = completionTime,
            streak = calculateStreak(oldInstance.habitId, oldInstance.date)
        )
        println("Updating habit instance: $instance")
        habitInstanceDao.updateHabitInstance(instance)
        updateNextInstance(instance)
    }

    override suspend fun deleteHabitInstance(habitInstance: HabitInstance) {
        habitInstanceDao.deleteHabitInstance(habitInstance)
    }

    override suspend fun getLastCompletedInstance(
        habitId: Int,
        date: LocalDate
    ): HabitInstance? {
        return habitInstanceDao.getLastCompletedInstance(habitId, date)
    }

    override suspend fun updateNextInstance(instance: HabitInstance) {
        val nextInstance =
            getHabitInstance(habitId = instance.habitId, date = instance.date.plusDays(1))
        if (nextInstance != null) {
            updateHabitInstance(
                instanceId = nextInstance.uid,
                isCompleted = nextInstance.isCompleted,
                completionTime = nextInstance.completionTime
            )
        }
    }

    private suspend fun calculateStreak(habitId: Int, date: LocalDate): Int {
        val lastInstance = getLastCompletedInstance(habitId, date)
        return if (lastInstance != null && lastInstance.date == date.minusDays(1)) {
            lastInstance.streak + 1 // ✅ Continue streak if last day was completed
        } else {
            1 // ✅ Reset streak if last day was missed
        }
    }
}


