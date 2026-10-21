package com.mindcheck.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.mindcheck.app.data.local.entity.HabitCompletionEntity
import com.mindcheck.app.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    // Habits
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1")
    suspend fun getHabitById(id: Long): HabitEntity?

    @Query("SELECT * FROM habits WHERE isArchived = 0 ORDER BY createdAt ASC")
    fun getActiveHabitsFlow(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits ORDER BY createdAt ASC")
    fun getAllHabitsFlow(): Flow<List<HabitEntity>>

    // Completions
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCompletion(completion: HabitCompletionEntity): Long

    @Upsert
    suspend fun upsertCompletion(completion: HabitCompletionEntity): Long

    @Delete
    suspend fun deleteCompletion(completion: HabitCompletionEntity)

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId AND completionDate = :date")
    suspend fun deleteCompletionByHabitAndDate(habitId: Long, date: String): Int

    @Query("SELECT * FROM habit_completions WHERE completionDate = :date")
    fun getCompletionsForDateFlow(date: String): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND completionDate = :date LIMIT 1")
    suspend fun getCompletion(habitId: Long, date: String): HabitCompletionEntity?

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY completionDate DESC")
    fun getCompletionsForHabitFlow(habitId: Long): Flow<List<HabitCompletionEntity>>

    @Query("SELECT COUNT(*) FROM habit_completions")
    suspend fun getCompletionsCount(): Int
}
