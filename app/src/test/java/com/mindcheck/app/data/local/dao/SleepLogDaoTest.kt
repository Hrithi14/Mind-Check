package com.mindcheck.app.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindcheck.app.data.local.database.MindCheckDatabase
import com.mindcheck.app.data.local.entity.SleepLogEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SleepLogDaoTest {

    private lateinit var database: MindCheckDatabase
    private lateinit var dao: SleepLogDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MindCheckDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.sleepLogDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndQueryById_success() = runBlocking {
        val sleepLog = SleepLogEntity(
            date = "2026-10-21",
            durationMinutes = 450,
            qualityRating = 4
        )
        val id = dao.insert(sleepLog)
        assertTrue(id > 0)

        val retrieved = dao.getById(id)
        assertNotNull(retrieved)
        assertEquals("2026-10-21", retrieved?.date)
        assertEquals(450, retrieved?.durationMinutes)
        assertEquals(4, retrieved?.qualityRating)
    }

    @Test
    fun getLogsForDate_returnsMatchingEntries() = runBlocking {
        dao.insert(SleepLogEntity(date = "2026-10-21", durationMinutes = 480, qualityRating = 5))
        dao.insert(SleepLogEntity(date = "2026-10-20", durationMinutes = 400, qualityRating = 3))

        val logs = dao.getLogsForDateFlow("2026-10-21").first()
        assertEquals(1, logs.size)
        assertEquals(480, logs[0].durationMinutes)
    }

    @Test
    fun updateAndQuery_success() = runBlocking {
        val id = dao.insert(SleepLogEntity(date = "2026-10-21", durationMinutes = 420, qualityRating = 3))
        val existing = dao.getById(id)
        assertNotNull(existing)

        dao.update(existing!!.copy(durationMinutes = 480, qualityRating = 4))

        val updated = dao.getById(id)
        assertEquals(480, updated?.durationMinutes)
        assertEquals(4, updated?.qualityRating)
    }

    @Test
    fun delete_removesLog() = runBlocking {
        val id = dao.insert(SleepLogEntity(date = "2026-10-21", durationMinutes = 420))
        val existing = dao.getById(id)
        assertNotNull(existing)

        dao.delete(existing!!)
        assertNull(dao.getById(id))
        assertEquals(0, dao.getCount())
    }
}
