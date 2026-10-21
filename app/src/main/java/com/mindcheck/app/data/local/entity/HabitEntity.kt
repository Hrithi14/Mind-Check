package com.mindcheck.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a student's daily wellbeing micro-habit.
 */
@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
