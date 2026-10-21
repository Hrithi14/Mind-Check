package com.mindcheck.app.data.repository

import com.mindcheck.app.data.local.dao.DailyCheckInDao
import com.mindcheck.app.data.local.entity.DailyCheckInEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class DailyCheckInRepositoryImpl(
    private val dailyCheckInDao: DailyCheckInDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DailyCheckInRepository {

    override fun getCheckInForDate(date: String): Flow<DailyCheckInEntity?> {
        return dailyCheckInDao.getCheckInByDateFlow(date).flowOn(ioDispatcher)
    }

    override suspend fun getCheckInForDateDirect(date: String): DailyCheckInEntity? {
        return withContext(ioDispatcher) {
            dailyCheckInDao.getCheckInByDate(date)
        }
    }

    override fun getAllCheckIns(): Flow<List<DailyCheckInEntity>> {
        return dailyCheckInDao.getAllCheckInsFlow().flowOn(ioDispatcher)
    }

    override fun getCheckInsBetween(startDate: String, endDate: String): Flow<List<DailyCheckInEntity>> {
        return dailyCheckInDao.getCheckInsBetweenFlow(startDate, endDate).flowOn(ioDispatcher)
    }

    override suspend fun saveCheckIn(checkIn: DailyCheckInEntity): Long {
        return withContext(ioDispatcher) {
            dailyCheckInDao.upsert(checkIn)
        }
    }

    override suspend fun insertStrict(checkIn: DailyCheckInEntity): Long {
        return withContext(ioDispatcher) {
            dailyCheckInDao.insert(checkIn)
        }
    }

    override suspend fun deleteCheckIn(checkIn: DailyCheckInEntity) {
        withContext(ioDispatcher) {
            dailyCheckInDao.delete(checkIn)
        }
    }
}
