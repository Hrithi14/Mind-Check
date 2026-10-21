package com.mindcheck.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a student's daily mood and stress reflection.
 * Enforces at most one check-in per local calendar date via a unique index.
 */
@Entity(
    tableName = "daily_check_ins",
    indices = [
        Index(value = ["date"], unique = true)
    ]
)
data class DailyCheckInEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // ISO-8601 local date formatted as YYYY-MM-DD
    val moodRating: Int, // 1 (Awful) to 5 (Great)
    val stressRating: Int, // 1 (Very Low) to 10 (Extreme)
    val energyRating: Int, // 1 (Depleted) to 5 (Energized)
    val note: String? = null, // Optional brief personal reflection
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
