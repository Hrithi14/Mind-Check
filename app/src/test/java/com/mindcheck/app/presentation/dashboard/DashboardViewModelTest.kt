package com.mindcheck.app.presentation.dashboard

import com.mindcheck.app.presentation.focus.FocusArea
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DashboardViewModelTest {

    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        viewModel = DashboardViewModel()
    }

    @Test
    fun initialState_hasGreetingAndDefaultFocusAreas() {
        val state = viewModel.uiState.value
        assertNotNull(state.greeting)
        assertTrue(state.greeting.isNotEmpty())
        assertEquals("--", state.scorePlaceholderText)
        assertTrue(state.activeFocusAreas.isNotEmpty())
    }

    @Test
    fun setFocusAreas_updatesActiveFocusAreas() {
        val newAreas = setOf(FocusArea.HABITS, FocusArea.FITNESS)
        viewModel.setFocusAreas(newAreas)
        val state = viewModel.uiState.value
        assertEquals(2, state.activeFocusAreas.size)
        assertTrue(state.activeFocusAreas.contains(FocusArea.HABITS))
        assertTrue(state.activeFocusAreas.contains(FocusArea.FITNESS))
    }
}
