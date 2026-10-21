package com.mindcheck.app.data.local.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseMigrationTest {

    @Test
    fun migration1to2_addsTagsColumnSuccessfully() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbName = "migration-test.db"
        context.deleteDatabase(dbName)

        val helperConfig = SupportSQLiteOpenHelper.Configuration.builder(context)
            .name(dbName)
            .callback(object : SupportSQLiteOpenHelper.Callback(1) {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        """
                        CREATE TABLE IF NOT EXISTS daily_check_ins (
                            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            date TEXT NOT NULL,
                            moodRating INTEGER NOT NULL,
                            stressRating INTEGER NOT NULL,
                            energyRating INTEGER NOT NULL,
                            note TEXT,
                            createdAt INTEGER NOT NULL,
                            updatedAt INTEGER NOT NULL
                        )
                        """.trimIndent()
                    )
                }

                override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {}
            })
            .build()

        val helper = FrameworkSQLiteOpenHelperFactory().create(helperConfig)
        val db = helper.writableDatabase

        // Execute MIGRATION_1_2
        DatabaseMigrations.MIGRATION_1_2.migrate(db)

        // Verify tags column exists in table info
        val cursor = db.query("PRAGMA table_info(daily_check_ins)")
        var hasTagsColumn = false
        while (cursor.moveToNext()) {
            val columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            if (columnName == "tags") {
                hasTagsColumn = true
                break
            }
        }
        cursor.close()
        db.close()
        context.deleteDatabase(dbName)

        assertTrue("Expected 'tags' column to be present after running migration 1 to 2", hasTagsColumn)
    }
}
