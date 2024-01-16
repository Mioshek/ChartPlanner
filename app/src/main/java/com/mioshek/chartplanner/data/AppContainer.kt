package com.mioshek.chartplanner.data


import android.content.Context
import com.mioshek.chartplanner.data.models.AppDatabase
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import com.mioshek.chartplanner.data.models.habits.OfflineHabitsRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: HabitsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: HabitsRepository by lazy {
        OfflineHabitsRepository(AppDatabase.getDatabase(context).habitDao)
    }
}