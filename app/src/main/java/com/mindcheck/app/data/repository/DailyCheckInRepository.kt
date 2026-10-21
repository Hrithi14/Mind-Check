package com.mindcheck.app.data.repository

import com.mindcheck.app.data.local.entity.DailyCheckInEntity
import kotlinx.coroutines.flow.Flow

interface DailyCheckInRepository {
    fun getCheckInForDate(date: String): Flow<DailyCheckInEntity?>
    suspend fun getCheckInForDateDirect(date: String): DailyCheckInEntity?
    fun getAllCheckIns(): Flow<List<DailyCheckInEntity>>
    fun getCheckInsBetween(startDate: String, endDate: String): Flow<List<DailyCheckInEntity>>
    suspend fun saveCheckIn(checkIn: DailyCheckInEntity): Long
    suspend fun insertStrict(checkIn: DailyCheckInEntity): Long
    suspend fun deleteCheckIn(checkIn: DailyCheckInEntity)
}
