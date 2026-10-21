package com.mindcheck.app.data.local.dao

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindcheck.app.data.local.database.MindCheckDatabase
import com.mindcheck.app.data.local.entity.DailyCheckInEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DailyCheckInDaoTest {

    private lateinit var database: MindCheckDatabase
    private lateinit var dao: DailyCheckInDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MindCheckDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.dailyCheckInDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndQuery_success() = runBlocking {
        val entry = DailyCheckInEntity(
            date = "2026-10-21",
            moodRating = 4,
            stressRating = 3,
            energyRating = 4,
            note = "Focused study session"
        )
        val id = dao.insert(entry)
        assertTrue(id > 0)

        val retrieved = dao.getCheckInByDate("2026-10-21")
        assertNotNull(retrieved)
        assertEquals("2026-10-21", retrieved?.date)
        assertEquals(4, retrieved?.moodRating)
        assertEquals(3, retrieved?.stressRating)
        assertEquals(4, retrieved?.energyRating)
        assertEquals("Focused study session", retrieved?.note)
    }

    @Test
    fun uniquenessConstraint_rejectsDuplicateDate() = runBlocking {
        val entry1 = DailyCheckInEntity(
            date = "2026-10-21",
            moodRating = 4,
            stressRating = 3,
            energyRating = 4
        )
        dao.insert(entry1)

        val entry2 = DailyCheckInEntity(
            date = "2026-10-21", // Duplicate date
            moodRating = 2,
            stressRating = 7,
            energyRating = 2
        )

        try {
            dao.insert(entry2)
            fail("Expected SQLiteConstraintException when inserting duplicate date")
        } catch (e: SQLiteConstraintException) {
            // Expected behavior
            assertTrue(true)
        }
    }

    @Test
    fun upsert_updatesExistingDateCheckIn() = runBlocking {
        val entry1 = DailyCheckInEntity(
            date = "2026-10-21",
            moodRating = 3,
            stressRating = 6,
            energyRating = 3
        )
        dao.upsert(entry1)

        val existing = dao.getCheckInByDate("2026-10-21")
        assertNotNull(existing)

        val updated = existing!!.copy(moodRating = 5, note = "Felt much better in evening")
        dao.upsert(updated)

        val result = dao.getCheckInByDate("2026-10-21")
        assertEquals(5, result?.moodRating)
        assertEquals("Felt much better in evening", result?.note)
        assertEquals(1, dao.getCount())
    }

    @Test
    fun delete_removesCheckIn() = runBlocking {
        val entry = DailyCheckInEntity(
            date = "2026-10-21",
            moodRating = 4,
            stressRating = 3,
            energyRating = 4
        )
        dao.insert(entry)
        val inserted = dao.getCheckInByDate("2026-10-21")
        assertNotNull(inserted)

        dao.delete(inserted!!)
        val result = dao.getCheckInByDate("2026-10-21")
        assertNull(result)
        assertEquals(0, dao.getCount())
    }

    @Test
    fun queryBetweenDates_returnsOnlyMatchingRange() = runBlocking {
        dao.insert(DailyCheckInEntity(date = "2026-10-18", moodRating = 3, stressRating = 4, energyRating = 3))
        dao.insert(DailyCheckInEntity(date = "2026-10-19", moodRating = 4, stressRating = 3, energyRating = 4))
        dao.insert(DailyCheckInEntity(date = "2026-10-20", moodRating = 2, stressRating = 7, energyRating = 2))
        dao.insert(DailyCheckInEntity(date = "2026-10-21", moodRating = 5, stressRating = 2, energyRating = 5))

        val list = dao.getCheckInsBetweenFlow("2026-10-19", "2026-10-20").first()
        assertEquals(2, list.size)
        assertEquals("2026-10-19", list[0].date)
        assertEquals("2026-10-20", list[1].date)
    }
}
