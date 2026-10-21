package com.mindcheck.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tracks the completion status of a habit on a specific local date.
 * Foreign key cascades deletions if the parent habit is deleted.
 * Unique composite index prevents duplicate logs for the same habit and date.
 */
@Entity(
    tableName = "habit_completions",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["habitId", "completionDate"], unique = true),
        Index(value = ["completionDate"])
    ]
)
data class HabitCompletionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val habitId: Long,
    val completionDate: String, // Local date (YYYY-MM-DD)
    val isCompleted: Boolean = true,
    val completedAt: Long = System.currentTimeMillis()
)
