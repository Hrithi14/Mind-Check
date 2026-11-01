package com.mindcheck.app.data.repository

import com.mindcheck.app.data.local.entity.WellnessReportEntity
import kotlinx.coroutines.flow.Flow

interface WellnessReportRepository {
    fun getAllReports(): Flow<List<WellnessReportEntity>>
    suspend fun getReportById(id: Long): WellnessReportEntity?
    suspend fun generateInstantReport(): WellnessReportEntity
    suspend fun saveReport(report: WellnessReportEntity): Long
    suspend fun deleteReport(id: Long)
    fun formatReportForExport(report: WellnessReportEntity): String
}
