package com.mindcheck.app.data.repository

import com.mindcheck.app.data.local.entity.SleepLogEntity
import kotlinx.coroutines.flow.Flow

interface SleepRepository {
    fun getSleepLogsForDate(date: String): Flow<List<SleepLogEntity>>
    suspend fun getLatestSleepLogForDate(date: String): SleepLogEntity?
    fun getAllSleepLogs(): Flow<List<SleepLogEntity>>
    fun getSleepLogsBetween(startDate: String, endDate: String): Flow<List<SleepLogEntity>>
    suspend fun saveSleepLog(sleepLog: SleepLogEntity): Long
    suspend fun deleteSleepLog(sleepLog: SleepLogEntity)
}
