package com.mindcheck.app.data.repository

import com.mindcheck.app.data.local.dao.HabitDao
import com.mindcheck.app.data.local.entity.HabitCompletionEntity
import com.mindcheck.app.data.local.entity.HabitEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class HabitRepositoryImpl(
    private val habitDao: HabitDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : HabitRepository {

    override fun getActiveHabits(): Flow<List<HabitEntity>> {
        return habitDao.getActiveHabitsFlow().flowOn(ioDispatcher)
    }

    override fun getAllHabits(): Flow<List<HabitEntity>> {
        return habitDao.getAllHabitsFlow().flowOn(ioDispatcher)
    }

    override suspend fun getHabitById(id: Long): HabitEntity? {
        return withContext(ioDispatcher) {
            habitDao.getHabitById(id)
        }
    }

    override suspend fun createHabit(title: String, description: String?): Long {
        return withContext(ioDispatcher) {
            habitDao.insertHabit(
                HabitEntity(
                    title = title,
                    description = description
                )
            )
        }
    }

    override suspend fun updateHabit(habit: HabitEntity) {
        withContext(ioDispatcher) {
            habitDao.updateHabit(habit)
        }
    }

    override suspend fun setHabitArchived(habitId: Long, isArchived: Boolean) {
        withContext(ioDispatcher) {
            val existing = habitDao.getHabitById(habitId)
            if (existing != null) {
                habitDao.updateHabit(existing.copy(isArchived = isArchived))
            }
        }
    }

    override suspend fun deleteHabit(habit: HabitEntity) {
        withContext(ioDispatcher) {
            habitDao.deleteHabit(habit)
        }
    }

    override fun getCompletionsForDate(date: String): Flow<List<HabitCompletionEntity>> {
        return habitDao.getCompletionsForDateFlow(date).flowOn(ioDispatcher)
    }

    override fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletionEntity>> {
        return habitDao.getCompletionsForHabitFlow(habitId).flowOn(ioDispatcher)
    }

    override suspend fun setHabitCompletion(habitId: Long, date: String, isCompleted: Boolean): Long {
        return withContext(ioDispatcher) {
            val existing = habitDao.getCompletion(habitId, date)
            if (existing != null) {
                habitDao.upsertCompletion(existing.copy(isCompleted = isCompleted))
            } else {
                habitDao.insertCompletion(
                    HabitCompletionEntity(
                        habitId = habitId,
                        completionDate = date,
                        isCompleted = isCompleted
                    )
                )
            }
        }
    }

    override suspend fun removeHabitCompletion(habitId: Long, date: String): Int {
        return withContext(ioDispatcher) {
            habitDao.deleteCompletionByHabitAndDate(habitId, date)
        }
    }
}
