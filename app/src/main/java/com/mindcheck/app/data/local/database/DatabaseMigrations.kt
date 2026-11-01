package com.mindcheck.app.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    const val DATABASE_NAME = "mindcheck.db"
    const val CURRENT_VERSION = 2

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS wellness_reports (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    title TEXT NOT NULL,
                    dateRange TEXT NOT NULL,
                    categoryTag TEXT NOT NULL,
                    secondaryBadge TEXT NOT NULL,
                    description TEXT NOT NULL,
                    taskCompletionRate INTEGER NOT NULL,
                    tasksDoneCount INTEGER NOT NULL,
                    dailyAvgAppMinutes INTEGER NOT NULL,
                    appUsageCategory TEXT NOT NULL,
                    moodCalmPercent INTEGER NOT NULL,
                    moodHappyPercent INTEGER NOT NULL,
                    moodStressedPercent INTEGER NOT NULL,
                    moodTiredPercent INTEGER NOT NULL,
                    bloomInsight TEXT NOT NULL,
                    participantsCount INTEGER NOT NULL,
                    authorTag TEXT NOT NULL,
                    createdAt INTEGER NOT NULL
                )
                """.trimIndent()
            )
        }
    }

    val ALL_MIGRATIONS = arrayOf(
        MIGRATION_1_2
    )
}
