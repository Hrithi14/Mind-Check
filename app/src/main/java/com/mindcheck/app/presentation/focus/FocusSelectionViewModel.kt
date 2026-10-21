package com.mindcheck.app.presentation.focus

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FocusSelectionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FocusSelectionUiState())
    val uiState: StateFlow<FocusSelectionUiState> = _uiState.asStateFlow()

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
}
