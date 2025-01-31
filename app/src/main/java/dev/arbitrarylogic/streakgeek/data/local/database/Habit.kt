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

package dev.arbitrarylogic.streakgeek.data.local.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    val name: String,
    val description: String = "",
    val dateCreated: Long = System.currentTimeMillis()
)

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit ORDER BY uid DESC LIMIT 10")
    fun getHabits(): Flow<List<Habit>>

    @Insert
    suspend fun insertHabit(item: Habit)
}
