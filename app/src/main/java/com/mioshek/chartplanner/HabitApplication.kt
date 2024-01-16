package com.mioshek.chartplanner

import android.app.Application
import com.mioshek.chartplanner.data.AppContainer
import com.mioshek.chartplanner.data.AppDataContainer

class HabitApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}