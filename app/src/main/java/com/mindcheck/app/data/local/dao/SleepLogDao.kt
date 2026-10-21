package com.mindcheck.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mindcheck.app.data.local.entity.SleepLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sleepLog: SleepLogEntity): Long

    @Update
    suspend fun update(sleepLog: SleepLogEntity)

    @Delete
    suspend fun delete(sleepLog: SleepLogEntity)

    @Query("SELECT * FROM sleep_logs WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): SleepLogEntity?

    @Query("SELECT * FROM sleep_logs WHERE date = :date ORDER BY createdAt DESC")
    fun getLogsForDateFlow(date: String): Flow<List<SleepLogEntity>>

    @Query("SELECT * FROM sleep_logs WHERE date = :date ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestLogForDate(date: String): SleepLogEntity?

    @Query("SELECT * FROM sleep_logs ORDER BY date DESC, createdAt DESC")
    fun getAllLogsFlow(): Flow<List<SleepLogEntity>>

    @Query("SELECT * FROM sleep_logs WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getLogsBetweenFlow(startDate: String, endDate: String): Flow<List<SleepLogEntity>>

    @Query("SELECT COUNT(*) FROM sleep_logs")
    suspend fun getCount(): Int
}
