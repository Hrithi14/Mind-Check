package com.mindcheck.app.presentation.dashboard

import com.mindcheck.app.presentation.focus.FocusArea

data class DashboardUiState(
    val greeting: String = "Hello, Student 👋",
    val greetingSubtitle: String = "Here is your quiet space for today",
    val activeFocusAreas: List<FocusArea> = listOf(FocusArea.SLEEP, FocusArea.STRESS, FocusArea.SCROLLING),
    val isCheckInCompletedToday: Boolean = false,
    val scorePlaceholderText: String = "--",
    val scoreStatusDescription: String = "Wellbeing score calculation will activate in Phase 4 with real local data"
)
