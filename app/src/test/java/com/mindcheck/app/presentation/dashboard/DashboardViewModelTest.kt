package com.mindcheck.app.presentation.dashboard

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindcheck.app.data.local.entity.DailyCheckInEntity
import com.mindcheck.app.data.repository.DailyCheckInRepository
import com.mindcheck.app.presentation.focus.FocusArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DashboardViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DashboardViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_hasGreetingAndDefaultFocusAreas() {
        val state = viewModel.uiState.value
        assertNotNull(state.greeting)
        assertTrue(state.greeting.isNotEmpty())
        assertEquals("--", state.scorePlaceholderText)
        assertTrue(state.activeFocusAreas.isNotEmpty())
        assertFalse(state.isCheckInCompletedToday)
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

    @Test
    fun repositoryBacked_reflectsTodayCheckInStatus() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val mockCheckIn = DailyCheckInEntity(date = today, moodRating = 5, stressRating = 1, energyRating = 5)

        val fakeRepo = object : DailyCheckInRepository {
            override fun getCheckInForDate(date: String): Flow<DailyCheckInEntity?> = flowOf(mockCheckIn)
            override suspend fun getCheckInForDateDirect(date: String): DailyCheckInEntity? = mockCheckIn
            override fun getAllCheckIns(): Flow<List<DailyCheckInEntity>> = flowOf(listOf(mockCheckIn))
            override fun getCheckInsBetween(startDate: String, endDate: String): Flow<List<DailyCheckInEntity>> = flowOf(emptyList())
            override suspend fun saveCheckIn(checkIn: DailyCheckInEntity): Long = 1L
            override suspend fun insertStrict(checkIn: DailyCheckInEntity): Long = 1L
            override suspend fun deleteCheckIn(checkIn: DailyCheckInEntity) {}
        }

        val vmWithRepo = DashboardViewModel(dailyCheckInRepository = fakeRepo)
        assertTrue(vmWithRepo.uiState.value.isCheckInCompletedToday)
    }
}
