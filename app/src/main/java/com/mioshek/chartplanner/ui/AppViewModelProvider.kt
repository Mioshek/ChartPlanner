package com.mioshek.chartplanner.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mioshek.chartplanner.HabitApplication
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import com.mioshek.chartplanner.views.habits.HabitViewModel
import com.mioshek.chartplanner.views.habits.ListHabitsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ListHabitsViewModel(
                this.createSavedStateHandle(),
                habitApplication().container.itemsRepository
            )
        }

        initializer {
            HabitViewModel(
                this.createSavedStateHandle(),
                habitApplication().container.itemsRepository
            )
        }
    }
}

fun CreationExtras.habitApplication(): HabitApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HabitApplication)