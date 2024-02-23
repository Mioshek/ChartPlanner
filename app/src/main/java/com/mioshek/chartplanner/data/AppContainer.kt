package com.mioshek.chartplanner.data


import android.content.Context
import com.mioshek.chartplanner.data.models.AppDatabase
import com.mioshek.chartplanner.data.models.habits.CompletedRepository
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import com.mioshek.chartplanner.data.models.habits.OfflineCompletedRepository
import com.mioshek.chartplanner.data.models.habits.OfflineHabitsRepository
import com.mioshek.chartplanner.data.models.settings.OfflineSettingsRepository
import com.mioshek.chartplanner.data.models.settings.SettingsRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val habitsRepository: HabitsRepository
    val completedRepository: CompletedRepository
    val settingsRepository: SettingsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val habitsRepository: HabitsRepository by lazy {
        OfflineHabitsRepository(AppDatabase.getDatabase(context).habitDao)
    }

    override val completedRepository: CompletedRepository by lazy{
        OfflineCompletedRepository(AppDatabase.getDatabase(context).completedDao)
    }

    override val settingsRepository: SettingsRepository by lazy{
        OfflineSettingsRepository(AppDatabase.getDatabase(context).settingsDao)
    }
}