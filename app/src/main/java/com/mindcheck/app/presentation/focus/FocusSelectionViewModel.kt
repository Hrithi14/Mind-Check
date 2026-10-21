package com.mindcheck.app.presentation.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindcheck.app.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FocusSelectionViewModel(
    private val preferencesRepository: UserPreferencesRepository? = null
) : ViewModel() {
    private val _uiState = MutableStateFlow(FocusSelectionUiState())
    val uiState: StateFlow<FocusSelectionUiState> = _uiState.asStateFlow()

    init {
        preferencesRepository?.let { repo ->
            viewModelScope.launch {
                repo.selectedFocusAreas.collect { storedAreas ->
                    if (storedAreas.isNotEmpty()) {
                        _uiState.update { it.copy(selectedAreas = storedAreas) }
                    }
                }
            }
        }
    }

    fun toggleFocusArea(area: FocusArea) {
        _uiState.update { current ->
            val updatedSelection = if (current.selectedAreas.contains(area)) {
                current.selectedAreas - area
            } else {
                current.selectedAreas + area
            }
            current.copy(selectedAreas = updatedSelection)
        }
    }

    fun selectAll() {
        _uiState.update { current ->
            current.copy(selectedAreas = FocusArea.entries.toSet())
        }
    }

    fun clearAll() {
        _uiState.update { current ->
            current.copy(selectedAreas = emptySet())
        }
    }

    fun saveSelection(onComplete: () -> Unit = {}) {
        val selected = _uiState.value.selectedAreas
        preferencesRepository?.let { repo ->
            viewModelScope.launch {
                repo.setSelectedFocusAreas(selected)
                onComplete()
            }
        } ?: onComplete()
    }
}
