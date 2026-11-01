package com.mindcheck.app.presentation.report

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindcheck.app.data.local.entity.WellnessReportEntity
import com.mindcheck.app.data.repository.WellnessReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class WellnessReportsViewModel(
    private val repository: WellnessReportRepository?
) : ViewModel() {

    private val _uiState = MutableStateFlow(WellnessReportsUiState(isLoading = true))
    val uiState: StateFlow<WellnessReportsUiState> = _uiState.asStateFlow()

    init {
        loadReports()
    }

    private fun loadReports() {
        if (repository == null) {
            _uiState.update {
                it.copy(
                    reports = listOf(createDefaultInitialReport()),
                    isLoading = false
                )
            }
            return
        }

        viewModelScope.launch {
            repository.getAllReports().collect { reportList ->
                if (reportList.isEmpty()) {
                    // Seed initial report matching the user's design reference
                    repository.saveReport(createDefaultInitialReport())
                } else {
                    _uiState.update { it.copy(reports = reportList, isLoading = false) }
                }
            }
        }
    }

    fun generateInstantReport() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                if (repository != null) {
                    val newReport = repository.generateInstantReport()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "Instant report generated for ${newReport.dateRange}!"
                        )
                    }
                } else {
                    val newReport = createDefaultInitialReport().copy(
                        id = System.currentTimeMillis(),
                        title = "Instant Wellness Pulse",
                        dateRange = "Generated Just Now",
                        createdAt = System.currentTimeMillis()
                    )
                    _uiState.update {
                        it.copy(
                            reports = listOf(newReport) + it.reports,
                            isLoading = false,
                            userMessage = "Instant report generated!"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userMessage = "Failed to generate report: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun viewReportDetails(report: WellnessReportEntity) {
        _uiState.update { it.copy(selectedReportForDetails = report) }
    }

    fun dismissDetails() {
        _uiState.update { it.copy(selectedReportForDetails = null) }
    }

    fun dismissMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun downloadReport(context: Context, report: WellnessReportEntity) {
        viewModelScope.launch {
            try {
                val reportContent = repository?.formatReportForExport(report)
                    ?: formatFallbackExport(report)
                val fileName = "MindCheck_Wellness_Report_${report.id}_${System.currentTimeMillis()}.txt"

                val success = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val resolver = context.contentResolver
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/MindCheck")
                    }
                    val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    if (uri != null) {
                        resolver.openOutputStream(uri)?.use { outputStream ->
                            outputStream.write(reportContent.toByteArray())
                        }
                        true
                    } else {
                        false
                    }
                } else {
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val targetDir = File(downloadsDir, "MindCheck").apply { if (!exists()) mkdirs() }
                    val file = File(targetDir, fileName)
                    FileOutputStream(file).use { outputStream ->
                        outputStream.write(reportContent.toByteArray())
                    }
                    true
                }

                if (success) {
                    _uiState.update {
                        it.copy(userMessage = "Downloaded to Downloads/MindCheck: $fileName")
                    }
                } else {
                    _uiState.update {
                        it.copy(userMessage = "Could not save report to device storage.")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = "Download failed: ${e.localizedMessage}")
                }
            }
        }
    }

    private fun createDefaultInitialReport(): WellnessReportEntity {
        return WellnessReportEntity(
            title = "7-Day Mindful Breathing & Wellbeing",
            dateRange = "Past 7 days · Oct 25 – Nov 01",
            categoryTag = "Mindfulness",
            secondaryBadge = "Popular",
            description = "Find calm and reset stress in just 5 mindful minutes a day. Recorded 82% task completion with healthy usage.",
            taskCompletionRate = 82,
            tasksDoneCount = 23,
            dailyAvgAppMinutes = 46,
            appUsageCategory = "Healthy usage",
            moodCalmPercent = 45,
            moodHappyPercent = 30,
            moodStressedPercent = 15,
            moodTiredPercent = 10,
            bloomInsight = "Your highest focus streak was recorded on mid-week afternoons right after using Guided Breathing. Keeping study sessions to 25 minutes helped you complete 4 more goals than average!",
            participantsCount = 4210,
            authorTag = "Elena Mindful · Bloom Engine",
            createdAt = System.currentTimeMillis() - 86400000L
        )
    }

    private fun formatFallbackExport(report: WellnessReportEntity): String {
        return """
            MIND CHECK — WELLNESS REPORT
            =============================
            Title: ${report.title}
            Date: ${report.dateRange}
            Tag: ${report.categoryTag}
            
            SUMMARY METRICS:
            - Task Completion: ${report.taskCompletionRate}% (${report.tasksDoneCount} tasks done)
            - Daily Avg App Time: ${report.dailyAvgAppMinutes}m (${report.appUsageCategory})
            
            MOOD DISTRIBUTION:
            - Calm / Peaceful:   ${report.moodCalmPercent}%
            - Happy / Energized: ${report.moodHappyPercent}%
            - Stressed / Rushed: ${report.moodStressedPercent}%
            - Tired / Low Energy: ${report.moodTiredPercent}%
            
            BLOOM WEEKLY INSIGHT:
            ${report.bloomInsight}
        """.trimIndent()
    }
}
