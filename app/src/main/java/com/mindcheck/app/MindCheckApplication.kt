package com.mindcheck.app

import android.app.Application
import com.mindcheck.app.di.AppContainer
import com.mindcheck.app.di.DefaultAppContainer

class MindCheckApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
