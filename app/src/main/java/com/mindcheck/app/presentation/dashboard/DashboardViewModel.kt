package com.mindcheck.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindcheck.app.data.repository.DailyCheckInRepository
import com.mindcheck.app.data.repository.UserPreferencesRepository
import com.mindcheck.app.presentation.focus.FocusArea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DashboardViewModel(
    private val dailyCheckInRepository: DailyCheckInRepository? = null,
    private val preferencesRepository: UserPreferencesRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        updateGreeting()
        observeStoredPreferences()
        observeTodayCheckIn()
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

    private fun observeStoredPreferences() {
        preferencesRepository?.let { repo ->
            viewModelScope.launch {
                repo.selectedFocusAreas.collect { storedAreas ->
                    if (storedAreas.isNotEmpty()) {
                        _uiState.update {
                            it.copy(activeFocusAreas = storedAreas.toList())
                        }
                    }
                }
            }
        }
    }

    private fun observeTodayCheckIn() {
        dailyCheckInRepository?.let { repo ->
            val today = getTodayLocalDateString()
            viewModelScope.launch {
                repo.getCheckInForDate(today).collect { checkIn ->
                    _uiState.update {
                        it.copy(isCheckInCompletedToday = checkIn != null)
                    }
                }
            }
        }
    }

    fun setFocusAreas(areas: Set<FocusArea>) {
        if (areas.isNotEmpty()) {
            _uiState.update {
                it.copy(activeFocusAreas = areas.toList())
            }
            preferencesRepository?.let { repo ->
                viewModelScope.launch {
                    repo.setSelectedFocusAreas(areas)
                }
            }
        }
    }

    private fun getTodayLocalDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}
