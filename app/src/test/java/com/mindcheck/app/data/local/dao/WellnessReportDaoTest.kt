package com.mindcheck.app.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindcheck.app.data.local.database.MindCheckDatabase
import com.mindcheck.app.data.local.entity.WellnessReportEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WellnessReportDaoTest {

    private lateinit var database: MindCheckDatabase
    private lateinit var dao: WellnessReportDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MindCheckDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.wellnessReportDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndRetrieveReports_orderedByCreatedAtDesc() = runBlocking {
        val r1 = WellnessReportEntity(
            title = "Report 1",
            dateRange = "Week 1",
            description = "First test report",
            createdAt = 1000L,
            bloomInsight = "Insight 1"
        )
        val r2 = WellnessReportEntity(
            title = "Report 2",
            dateRange = "Week 2",
            description = "Second test report",
            createdAt = 2000L,
            bloomInsight = "Insight 2"
        )

        dao.insertReport(r1)
        dao.insertReport(r2)

        val allReports = dao.getAllReports().first()
        assertEquals(2, allReports.size)
        // Ensure newest is first
        assertEquals("Report 2", allReports[0].title)
        assertEquals("Report 1", allReports[1].title)
    }

    @Test
    fun deleteReportById_removesItem() = runBlocking {
        val r = WellnessReportEntity(
            title = "Deletable Report",
            dateRange = "Today",
            description = "Testing delete",
            createdAt = 3000L,
            bloomInsight = "Insight"
        )
        val id = dao.insertReport(r)
        assertNotNull(dao.getReportById(id))

        dao.deleteReportById(id)
        val list = dao.getAllReports().first()
        assertTrue(list.none { it.id == id })
    }
}
