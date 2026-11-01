package com.mindcheck.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mindcheck.app.data.local.entity.WellnessReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WellnessReportDao {

    @Query("SELECT * FROM wellness_reports ORDER BY createdAt DESC")
    fun getAllReports(): Flow<List<WellnessReportEntity>>

    @Query("SELECT * FROM wellness_reports WHERE id = :id LIMIT 1")
    suspend fun getReportById(id: Long): WellnessReportEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: WellnessReportEntity): Long

    @Query("DELETE FROM wellness_reports WHERE id = :id")
    suspend fun deleteReportById(id: Long)

    @Query("DELETE FROM wellness_reports")
    suspend fun deleteAllReports()
}
