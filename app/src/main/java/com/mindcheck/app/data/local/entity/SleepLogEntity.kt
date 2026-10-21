package com.mindcheck.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a logged sleep session.
 * Indexed by date for fast history retrieval.
 */
@Entity(
    tableName = "sleep_logs",
    indices = [
        Index(value = ["date"])
    ]
)
data class SleepLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // Associated local calendar date (YYYY-MM-DD)
    val durationMinutes: Int, // Total sleep duration in minutes
    val qualityRating: Int? = null, // Optional quality rating 1 (Poor) to 5 (Restful)
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
