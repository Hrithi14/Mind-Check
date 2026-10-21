package com.mindcheck.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mindcheck.app.data.local.dao.DailyCheckInDao
import com.mindcheck.app.data.local.dao.HabitDao
import com.mindcheck.app.data.local.dao.SleepLogDao
import com.mindcheck.app.data.local.entity.DailyCheckInEntity
import com.mindcheck.app.data.local.entity.HabitCompletionEntity
import com.mindcheck.app.data.local.entity.HabitEntity
import com.mindcheck.app.data.local.entity.SleepLogEntity

@Database(
    entities = [
        DailyCheckInEntity::class,
        SleepLogEntity::class,
        HabitEntity::class,
        HabitCompletionEntity::class
    ],
    version = DatabaseMigrations.CURRENT_VERSION,
    exportSchema = false
)
abstract class MindCheckDatabase : RoomDatabase() {

    abstract fun dailyCheckInDao(): DailyCheckInDao
    abstract fun sleepLogDao(): SleepLogDao
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var INSTANCE: MindCheckDatabase? = null

        fun getInstance(context: Context): MindCheckDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): MindCheckDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MindCheckDatabase::class.java,
                DatabaseMigrations.DATABASE_NAME
            )
                .addMigrations(*DatabaseMigrations.ALL_MIGRATIONS)
                .build()
        }
    }
}
