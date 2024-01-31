package com.mioshek.chartplanner.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mioshek.chartplanner.HabitApplication
import com.mioshek.chartplanner.views.charts.ChartViewModel
import com.mioshek.chartplanner.views.habits.HabitViewModel
import com.mioshek.chartplanner.views.habits.ListHabitsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ListHabitsViewModel(
                this.createSavedStateHandle(),
                habitApplication().container.habitsRepository,
                habitApplication().container.completedRepository
            )
        }

        initializer {
            HabitViewModel(
                this.createSavedStateHandle(),
                habitApplication().container.habitsRepository,
                habitApplication().container.completedRepository
            )
        }

        initializer {
            ChartViewModel(
                this.createSavedStateHandle(),
                habitApplication().container.habitsRepository,
                habitApplication().container.completedRepository
            )
        }
    }
}

fun CreationExtras.habitApplication(): HabitApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HabitApplication)