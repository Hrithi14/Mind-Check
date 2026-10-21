package com.mindcheck.app.presentation.focus

data class FocusSelectionUiState(
    val availableAreas: List<FocusArea> = FocusArea.entries,
    val selectedAreas: Set<FocusArea> = setOf(FocusArea.SLEEP, FocusArea.STRESS, FocusArea.SCROLLING),
    val isLoading: Boolean = false
) {
    val canProceed: Boolean get() = selectedAreas.isNotEmpty()
}
