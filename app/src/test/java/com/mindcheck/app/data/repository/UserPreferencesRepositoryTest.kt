package com.mindcheck.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindcheck.app.data.local.datastore.ThemePreference
import com.mindcheck.app.presentation.focus.FocusArea
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UserPreferencesRepositoryTest {

    private lateinit var testFile: File
    private lateinit var repository: UserPreferencesRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        testFile = File(context.filesDir, "test_preferences.preferences_pb")
        if (testFile.exists()) {
            testFile.delete()
        }

        val testDataStore = PreferenceDataStoreFactory.create(
            scope = TestScope(UnconfinedTestDispatcher()),
            produceFile = { testFile }
        )
        repository = UserPreferencesRepositoryImpl(testDataStore)
    }

    @After
    fun tearDown() {
        if (testFile.exists()) {
            testFile.delete()
        }
    }

    @Test
    fun defaultPreferences_haveSensibleValues() = runTest {
        assertFalse(repository.isOnboardingCompleted.first())
        val defaultAreas = repository.selectedFocusAreas.first()
        assertTrue(defaultAreas.contains(FocusArea.SLEEP))
        assertTrue(defaultAreas.contains(FocusArea.STRESS))
        assertEquals(ThemePreference.SYSTEM, repository.themePreference.first())
    }

    @Test
    fun setOnboardingCompleted_persistsFlag() = runTest {
        repository.setOnboardingCompleted(true)
        assertTrue(repository.isOnboardingCompleted.first())
    }

    @Test
    fun setSelectedFocusAreas_persistsCustomAreas() = runTest {
        val customAreas = setOf(FocusArea.FITNESS, FocusArea.HABITS)
        repository.setSelectedFocusAreas(customAreas)

        val retrieved = repository.selectedFocusAreas.first()
        assertEquals(2, retrieved.size)
        assertTrue(retrieved.contains(FocusArea.FITNESS))
        assertTrue(retrieved.contains(FocusArea.HABITS))
    }

    @Test
    fun setThemePreference_persistsThemeChoice() = runTest {
        repository.setThemePreference(ThemePreference.DARK)
        assertEquals(ThemePreference.DARK, repository.themePreference.first())

        repository.setThemePreference(ThemePreference.LIGHT)
        assertEquals(ThemePreference.LIGHT, repository.themePreference.first())
    }
}
