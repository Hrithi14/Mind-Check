package com.mindcheck.app.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "mindcheck_user_preferences"
)

object PreferenceKeys {
    val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
    val KEY_SELECTED_FOCUS_AREAS = stringSetPreferencesKey("selected_focus_areas")
    val KEY_THEME_PREFERENCE = stringPreferencesKey("theme_preference")
}
