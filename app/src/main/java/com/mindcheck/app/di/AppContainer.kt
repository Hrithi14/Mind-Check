package com.mindcheck.app.di

import android.content.Context
import com.mindcheck.app.data.local.database.MindCheckDatabase
import com.mindcheck.app.data.local.datastore.userPreferencesDataStore
import com.mindcheck.app.data.repository.DailyCheckInRepository
import com.mindcheck.app.data.repository.DailyCheckInRepositoryImpl
import com.mindcheck.app.data.repository.HabitRepository
import com.mindcheck.app.data.repository.HabitRepositoryImpl
import com.mindcheck.app.data.repository.SleepRepository
import com.mindcheck.app.data.repository.SleepRepositoryImpl
import com.mindcheck.app.data.repository.UserPreferencesRepository
import com.mindcheck.app.data.repository.UserPreferencesRepositoryImpl

interface AppContainer {
    val database: MindCheckDatabase
    val dailyCheckInRepository: DailyCheckInRepository
    val sleepRepository: SleepRepository
    val habitRepository: HabitRepository
    val userPreferencesRepository: UserPreferencesRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    override val database: MindCheckDatabase by lazy {
        MindCheckDatabase.getInstance(context)
    }

    override val dailyCheckInRepository: DailyCheckInRepository by lazy {
        DailyCheckInRepositoryImpl(database.dailyCheckInDao())
    }

    override val sleepRepository: SleepRepository by lazy {
        SleepRepositoryImpl(database.sleepLogDao())
    }

    override val habitRepository: HabitRepository by lazy {
        HabitRepositoryImpl(database.habitDao())
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepositoryImpl(context.userPreferencesDataStore)
    }
}
