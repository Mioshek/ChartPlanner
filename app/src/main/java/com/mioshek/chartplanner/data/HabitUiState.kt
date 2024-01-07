package com.mioshek.chartplanner.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

data class HabitUiState(
    val id: Int? = null,
    val name: String = "",
    val description: String? = null,
    val completed: Boolean = false,
    val intervalDays: Int? = null, // 1 means everyday
    val nextOccurring: Date? = null
)


class HabitViewModel: ViewModel() {
    private val _habitUiState = MutableStateFlow(HabitUiState())
    val habitUiState: StateFlow<HabitUiState> = _habitUiState.asStateFlow()

    private var logCallbackHabit: LogCallbackHabit? = null

    fun newHabit(newHabit: HabitUiState){ //same usage as edit habit
        _habitUiState.update { currentState ->
            currentState.copy(
                id = newHabit.id,
                name = newHabit.name,
                description = newHabit.description,
                completed = newHabit.completed,
                intervalDays = newHabit.intervalDays,
                nextOccurring = newHabit.nextOccurring
            )
        }
    }
}

typealias LogCallbackHabit = (String) -> Unit