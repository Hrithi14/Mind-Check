package com.mindcheck.app.data.repository

import com.mindcheck.app.data.local.entity.HabitCompletionEntity
import com.mindcheck.app.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getActiveHabits(): Flow<List<HabitEntity>>
    fun getAllHabits(): Flow<List<HabitEntity>>
    suspend fun getHabitById(id: Long): HabitEntity?
    suspend fun createHabit(title: String, description: String? = null): Long
    suspend fun updateHabit(habit: HabitEntity)
    suspend fun setHabitArchived(habitId: Long, isArchived: Boolean)
    suspend fun deleteHabit(habit: HabitEntity)

    fun getCompletionsForDate(date: String): Flow<List<HabitCompletionEntity>>
    fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletionEntity>>
    suspend fun setHabitCompletion(habitId: Long, date: String, isCompleted: Boolean): Long
    suspend fun removeHabitCompletion(habitId: Long, date: String): Int
}
