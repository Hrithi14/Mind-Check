package com.mindcheck.app.presentation.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindcheck.app.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val preferencesRepository: UserPreferencesRepository? = null
) : ViewModel() {
    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    fun completeWelcome() {
        preferencesRepository?.let { repo ->
            viewModelScope.launch {
                repo.setOnboardingCompleted(true)
            }
        }
    }
}
