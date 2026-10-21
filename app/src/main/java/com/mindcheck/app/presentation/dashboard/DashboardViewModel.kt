package com.mindcheck.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.mindcheck.app.presentation.focus.FocusArea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        updateGreeting()
    }

    private fun updateGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val (greeting, sub) = when (hour) {
            in 5..11 -> "Good morning, Student ☀️" to "Ready for a balanced day of learning?"
            in 12..16 -> "Good afternoon, Student 🌤️" to "Take a mindful breath between classes."
            in 17..21 -> "Good evening, Student 🌙" to "Time to unwind and process your day."
            else -> "Late night reflection 🌌" to "Remember that restful sleep restores focus."
        }
        _uiState.update {
            it.copy(
                greeting = greeting,
                greetingSubtitle = sub
            )
        }
    }

    fun setFocusAreas(areas: Set<FocusArea>) {
        if (areas.isNotEmpty()) {
            _uiState.update {
                it.copy(activeFocusAreas = areas.toList())
            }
        }
    }
}
