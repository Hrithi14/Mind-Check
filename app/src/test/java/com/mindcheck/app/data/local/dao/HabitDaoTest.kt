package com.mindcheck.app.data.local.dao

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindcheck.app.data.local.database.MindCheckDatabase
import com.mindcheck.app.data.local.entity.HabitCompletionEntity
import com.mindcheck.app.data.local.entity.HabitEntity
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
class HabitDaoTest {

    private lateinit var database: MindCheckDatabase
    private lateinit var dao: HabitDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MindCheckDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.habitDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndQueryActiveHabits() = runBlocking {
        val id1 = dao.insertHabit(HabitEntity(title = "Morning Sunlight", description = "10 mins outdoors"))
        val id2 = dao.insertHabit(HabitEntity(title = "Hydration", isArchived = true))

        val active = dao.getActiveHabitsFlow().first()
        assertEquals(1, active.size)
        assertEquals("Morning Sunlight", active[0].title)

        val all = dao.getAllHabitsFlow().first()
        assertEquals(2, all.size)
    }

    @Test
    fun insertHabitCompletion_success() = runBlocking {
        val habitId = dao.insertHabit(HabitEntity(title = "Drink Water"))
        val completionId = dao.insertCompletion(
            HabitCompletionEntity(habitId = habitId, completionDate = "2026-10-21", isCompleted = true)
        )
        assertTrue(completionId > 0)

        val completion = dao.getCompletion(habitId, "2026-10-21")
        assertNotNull(completion)
        assertEquals(true, completion?.isCompleted)
    }

    @Test
    fun uniquenessConstraint_rejectsDuplicateCompletionSameDate() = runBlocking {
        val habitId = dao.insertHabit(HabitEntity(title = "Study Block"))
        dao.insertCompletion(
            HabitCompletionEntity(habitId = habitId, completionDate = "2026-10-21", isCompleted = true)
        )

        try {
            dao.insertCompletion(
                HabitCompletionEntity(habitId = habitId, completionDate = "2026-10-21", isCompleted = true)
            )
            fail("Expected SQLiteConstraintException on duplicate habit completion date")
        } catch (e: SQLiteConstraintException) {
            assertTrue(true)
        }
    }

    @Test
    fun cascadeDelete_deletesCompletionsWhenHabitIsDeleted() = runBlocking {
        val habitId = dao.insertHabit(HabitEntity(title = "Evening Walk"))
        dao.insertCompletion(HabitCompletionEntity(habitId = habitId, completionDate = "2026-10-20"))
        dao.insertCompletion(HabitCompletionEntity(habitId = habitId, completionDate = "2026-10-21"))

        assertEquals(2, dao.getCompletionsCount())

        val habit = dao.getHabitById(habitId)
        assertNotNull(habit)
        dao.deleteHabit(habit!!)

        // Foreign key cascade must remove the associated completions
        assertEquals(0, dao.getCompletionsCount())
    }
}
