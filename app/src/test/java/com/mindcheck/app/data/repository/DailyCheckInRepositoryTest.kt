package com.mindcheck.app.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindcheck.app.data.local.database.MindCheckDatabase
import com.mindcheck.app.data.local.entity.DailyCheckInEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DailyCheckInRepositoryTest {

    private lateinit var database: MindCheckDatabase
    private lateinit var repository: DailyCheckInRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MindCheckDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = DailyCheckInRepositoryImpl(database.dailyCheckInDao(), testDispatcher)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveAndGetCheckInForDate() = runTest {
        val entry = DailyCheckInEntity(
            date = "2026-10-21",
            moodRating = 4,
            stressRating = 2,
            energyRating = 5,
            note = "Calm morning"
        )
        repository.saveCheckIn(entry)

        val retrieved = repository.getCheckInForDate("2026-10-21").first()
        assertNotNull(retrieved)
        assertEquals("2026-10-21", retrieved?.date)
        assertEquals(4, retrieved?.moodRating)
    }

    @Test
    fun deleteCheckIn_removesEntry() = runTest {
        val entry = DailyCheckInEntity(
            date = "2026-10-21",
            moodRating = 3,
            stressRating = 5,
            energyRating = 3
        )
        repository.saveCheckIn(entry)
        val saved = repository.getCheckInForDateDirect("2026-10-21")
        assertNotNull(saved)

        repository.deleteCheckIn(saved!!)
        val retrieved = repository.getCheckInForDateDirect("2026-10-21")
        assertNull(retrieved)
    }
}
