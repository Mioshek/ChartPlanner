package com.mioshek.chartplanner.data


import android.content.Context
import com.mioshek.chartplanner.data.models.AppDatabase
import com.mioshek.chartplanner.data.models.habits.CompletedRepository
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import com.mioshek.chartplanner.data.models.habits.OfflineCompletedRepository
import com.mioshek.chartplanner.data.models.habits.OfflineHabitsRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val habitsRepository: HabitsRepository
    val completedRepository: CompletedRepository
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
}