package dev.arbitrarylogic.streakgeek.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "habit_instance",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["uid"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE // Cascade delete instances when a habit is deleted
        )
    ],
    indices = [Index(value = ["habitId"])] // Add index for faster lookups
)
data class HabitInstance(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,                  // Auto-generated primary key
    val habitId: Int,                  // Foreign key linking to Habit
    val date: LocalDate,                  // Store LocalDate as ISO-8601 string
    val isCompleted: Boolean = false,  // Whether the habit is completed
    val notes: String = "",            // Optional notes
    val completionTime: LocalTime? = null, // Store LocalTime as ISO-8601 string
    val streak: Int = 1
)

@Dao
interface HabitInstanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitInstance(habitInstance: HabitInstance)

    @Update
    suspend fun updateHabitInstance(habitInstance: HabitInstance)

    @Delete
    suspend fun deleteHabitInstance(habitInstance: HabitInstance)

    @Query("SELECT * FROM habit_instance WHERE uid = :habitInstanceId LIMIT 1")
    suspend fun getHabitInstance(habitInstanceId: Int): HabitInstance?

    @Query("SELECT * FROM habit_instance WHERE habitId = :habitId AND date = :date LIMIT 1")
    suspend fun getHabitInstance(habitId: Int, date: LocalDate): HabitInstance?

    @Query("SELECT * FROM habit_instance WHERE habitId = :habitId ORDER BY date ASC")
    suspend fun getAllInstancesForHabit(habitId: Int): List<HabitInstance>

    @Query("SELECT * FROM habit_instance ORDER BY date ASC")
    fun getAllHabitInstances(): Flow<List<HabitInstance>>

    @Query("SELECT * FROM habit_instance WHERE date = :date")
    fun getHabitInstancesForDate(date: LocalDate): Flow<List<HabitInstance>>

    @Query("SELECT * FROM habit_instance WHERE habitId = :habitId AND isCompleted = 1 AND date < :today ORDER BY date DESC LIMIT 1")
    suspend fun getLastCompletedInstance(habitId: Int, today: LocalDate): HabitInstance?
}