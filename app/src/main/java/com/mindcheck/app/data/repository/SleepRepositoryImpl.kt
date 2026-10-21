package com.mindcheck.app.data.repository

import com.mindcheck.app.data.local.dao.SleepLogDao
import com.mindcheck.app.data.local.entity.SleepLogEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SleepRepositoryImpl(
    private val sleepLogDao: SleepLogDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SleepRepository {

    override fun getSleepLogsForDate(date: String): Flow<List<SleepLogEntity>> {
        return sleepLogDao.getLogsForDateFlow(date).flowOn(ioDispatcher)
    }

    override suspend fun getLatestSleepLogForDate(date: String): SleepLogEntity? {
        return withContext(ioDispatcher) {
            sleepLogDao.getLatestLogForDate(date)
        }
    }

    override fun getAllSleepLogs(): Flow<List<SleepLogEntity>> {
        return sleepLogDao.getAllLogsFlow().flowOn(ioDispatcher)
    }

    override fun getSleepLogsBetween(startDate: String, endDate: String): Flow<List<SleepLogEntity>> {
        return sleepLogDao.getLogsBetweenFlow(startDate, endDate).flowOn(ioDispatcher)
    }

    override suspend fun saveSleepLog(sleepLog: SleepLogEntity): Long {
        return withContext(ioDispatcher) {
            sleepLogDao.insert(sleepLog)
        }
    }

    override suspend fun deleteSleepLog(sleepLog: SleepLogEntity) {
        withContext(ioDispatcher) {
            sleepLogDao.delete(sleepLog)
        }
    }
}
