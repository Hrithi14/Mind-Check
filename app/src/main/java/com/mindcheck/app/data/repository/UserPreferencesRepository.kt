package com.mindcheck.app.data.repository

import com.mindcheck.app.data.local.datastore.ThemePreference
import com.mindcheck.app.presentation.focus.FocusArea
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isOnboardingCompleted: Flow<Boolean>
    val selectedFocusAreas: Flow<Set<FocusArea>>
    val themePreference: Flow<ThemePreference>

    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun setSelectedFocusAreas(areas: Set<FocusArea>)
    suspend fun setThemePreference(preference: ThemePreference)
}
