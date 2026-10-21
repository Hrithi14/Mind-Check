package com.mindcheck.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.mindcheck.app.data.local.datastore.PreferenceKeys
import com.mindcheck.app.data.local.datastore.ThemePreference
import com.mindcheck.app.presentation.focus.FocusArea
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    override val isOnboardingCompleted: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferenceKeys.KEY_ONBOARDING_COMPLETED] ?: false
        }

    override val selectedFocusAreas: Flow<Set<FocusArea>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val stored = preferences[PreferenceKeys.KEY_SELECTED_FOCUS_AREAS]
            if (stored.isNullOrEmpty()) {
                setOf(FocusArea.SLEEP, FocusArea.STRESS, FocusArea.SCROLLING)
            } else {
                stored.mapNotNull { name ->
                    try {
                        FocusArea.valueOf(name)
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                }.toSet()
            }
        }

    override val themePreference: Flow<ThemePreference> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val stored = preferences[PreferenceKeys.KEY_THEME_PREFERENCE]
            if (stored == null) {
                ThemePreference.SYSTEM
            } else {
                try {
                    ThemePreference.valueOf(stored)
                } catch (e: IllegalArgumentException) {
                    ThemePreference.SYSTEM
                }
            }
        }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.KEY_ONBOARDING_COMPLETED] = completed
        }
    }

    override suspend fun setSelectedFocusAreas(areas: Set<FocusArea>) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.KEY_SELECTED_FOCUS_AREAS] = areas.map { it.name }.toSet()
        }
    }

    override suspend fun setThemePreference(preference: ThemePreference) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.KEY_THEME_PREFERENCE] = preference.name
        }
    }
}
