package com.mindcheck.app.presentation.focus

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FocusSelectionViewModelTest {

    private lateinit var viewModel: FocusSelectionViewModel

    @Before
    fun setUp() {
        viewModel = FocusSelectionViewModel()
    }

    @Test
    fun defaultSelectedAreas_isNotEmpty() {
        val state = viewModel.uiState.value
        assertTrue(state.selectedAreas.isNotEmpty())
        assertTrue(state.canProceed)
    }

    @Test
    fun toggleFocusArea_removesWhenPresent() {
        viewModel.toggleFocusArea(FocusArea.SLEEP)
        val state = viewModel.uiState.value
        assertFalse(state.selectedAreas.contains(FocusArea.SLEEP))
    }

    @Test
    fun toggleFocusArea_addsWhenAbsent() {
        viewModel.clearAll()
        viewModel.toggleFocusArea(FocusArea.FITNESS)
        val state = viewModel.uiState.value
        assertTrue(state.selectedAreas.contains(FocusArea.FITNESS))
        assertEquals(1, state.selectedAreas.size)
    }

    @Test
    fun clearAll_disablesProceed() {
        viewModel.clearAll()
        val state = viewModel.uiState.value
        assertTrue(state.selectedAreas.isEmpty())
        assertFalse(state.canProceed)
    }

    @Test
    fun selectAll_selectsAllEnumEntries() {
        viewModel.clearAll()
        viewModel.selectAll()
        val state = viewModel.uiState.value
        assertEquals(FocusArea.entries.size, state.selectedAreas.size)
        assertTrue(state.canProceed)
    }
}
