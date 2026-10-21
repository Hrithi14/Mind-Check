package com.mindcheck.app.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    const val DATABASE_NAME = "mindcheck.db"
    const val CURRENT_VERSION = 1

    /**
     * Example forward migration from version 1 to 2.
     * Demonstrates safe schema alteration (adding a tags column to daily_check_ins)
     * without data loss.
     */
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE daily_check_ins ADD COLUMN tags TEXT DEFAULT NULL")
        }
    }

    val ALL_MIGRATIONS = arrayOf(
        MIGRATION_1_2
    )
}
