package com.mindcheck.app.presentation.report

import com.mindcheck.app.data.local.entity.WellnessReportEntity

data class WellnessReportsUiState(
    val reports: List<WellnessReportEntity> = emptyList(),
    val isLoading: Boolean = false,
    val selectedReportForDetails: WellnessReportEntity? = null,
    val userMessage: String? = null
)
