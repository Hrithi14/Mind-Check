package com.mindcheck.app.presentation.report

import com.mindcheck.app.data.local.entity.WellnessReportEntity
import com.mindcheck.app.data.repository.WellnessReportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WellnessReportsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeWellnessReportRepository
    private lateinit var viewModel: WellnessReportsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeWellnessReportRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_loadsReportsFromRepository() = runTest {
        viewModel = WellnessReportsViewModel(fakeRepository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertEquals(1, state.reports.size)
        assertEquals("7-Day Mindful Breathing & Wellbeing", state.reports[0].title)
    }

    @Test
    fun generateInstantReport_prependsNewReportAndEmitsMessage() = runTest {
        viewModel = WellnessReportsViewModel(fakeRepository)
        advanceUntilIdle()

        viewModel.generateInstantReport()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.reports.size)
        assertNotNull(state.userMessage)
    }

    @Test
    fun viewAndDismissDetails_updatesSelectedReport() = runTest {
        viewModel = WellnessReportsViewModel(fakeRepository)
        advanceUntilIdle()

        val report = viewModel.uiState.value.reports.first()
        viewModel.viewReportDetails(report)
        assertEquals(report.id, viewModel.uiState.value.selectedReportForDetails?.id)

        viewModel.dismissDetails()
        assertNull(viewModel.uiState.value.selectedReportForDetails)
    }

    private class FakeWellnessReportRepository : WellnessReportRepository {
        private val listFlow = MutableStateFlow<List<WellnessReportEntity>>(emptyList())

        override fun getAllReports(): Flow<List<WellnessReportEntity>> = listFlow

        override suspend fun getReportById(id: Long): WellnessReportEntity? =
            listFlow.value.find { it.id == id }

        override suspend fun saveReport(report: WellnessReportEntity): Long {
            val assignedId = if (report.id == 0L) (listFlow.value.size + 1).toLong() else report.id
            val saved = report.copy(id = assignedId)
            listFlow.value = listOf(saved) + listFlow.value
            return assignedId
        }

        override suspend fun generateInstantReport(): WellnessReportEntity {
            val newReport = WellnessReportEntity(
                id = (listFlow.value.size + 1).toLong(),
                title = "Instant Wellness & Focus Report",
                dateRange = "Generated Just Now",
                description = "Test description",
                taskCompletionRate = 88,
                tasksDoneCount = 26,
                dailyAvgAppMinutes = 42,
                bloomInsight = "Test insight"
            )
            listFlow.value = listOf(newReport) + listFlow.value
            return newReport
        }

        override suspend fun deleteReport(id: Long) {
            listFlow.value = listFlow.value.filterNot { it.id == id }
        }

        override fun formatReportForExport(report: WellnessReportEntity): String =
            "Report: ${report.title}"
    }
}
