package com.mindcheck.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.mindcheck.app.data.local.entity.DailyCheckInEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyCheckInDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(checkIn: DailyCheckInEntity): Long

    @Upsert
    suspend fun upsert(checkIn: DailyCheckInEntity): Long

    @Update
    suspend fun update(checkIn: DailyCheckInEntity)

    @Delete
    suspend fun delete(checkIn: DailyCheckInEntity)

    @Query("SELECT * FROM daily_check_ins WHERE date = :date LIMIT 1")
    fun getCheckInByDateFlow(date: String): Flow<DailyCheckInEntity?>

    @Query("SELECT * FROM daily_check_ins WHERE date = :date LIMIT 1")
    suspend fun getCheckInByDate(date: String): DailyCheckInEntity?

    @Query("SELECT * FROM daily_check_ins ORDER BY date DESC")
    fun getAllCheckInsFlow(): Flow<List<DailyCheckInEntity>>

    @Query("SELECT * FROM daily_check_ins WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getCheckInsBetweenFlow(startDate: String, endDate: String): Flow<List<DailyCheckInEntity>>

    @Query("SELECT COUNT(*) FROM daily_check_ins")
    suspend fun getCount(): Int
}
