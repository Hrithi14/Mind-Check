package com.mindcheck.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wellness_reports")
data class WellnessReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val dateRange: String,
    val categoryTag: String = "Mindfulness",
    val secondaryBadge: String = "Weekly Summary",
    val description: String,
    val taskCompletionRate: Int = 82,
    val tasksDoneCount: Int = 23,
    val dailyAvgAppMinutes: Int = 46,
    val appUsageCategory: String = "Healthy usage",
    val moodCalmPercent: Int = 45,
    val moodHappyPercent: Int = 30,
    val moodStressedPercent: Int = 15,
    val moodTiredPercent: Int = 10,
    val bloomInsight: String,
    val participantsCount: Int = 4210,
    val authorTag: String = "Mind Check Engine",
    val createdAt: Long = System.currentTimeMillis()
)
