package com.mioshek.chartplanner.views.habits

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ListHabitsUiState(
    val habits: List<HabitUiState> = listOf()
)


class ListHabitsViewModel: ViewModel() {
    private val _listHabitsUiState = MutableStateFlow(ListHabitsUiState())
    val habitUiState: StateFlow<ListHabitsUiState> = _listHabitsUiState.asStateFlow()

    private var logCallbackListHabits: LogCallbackListHabits? = null


    fun removeHabit(id: Int){
        _listHabitsUiState.update { currentState ->
            val newHabitList = currentState.habits.toMutableList()
            newHabitList.removeAt(id)
            val length = newHabitList.size
            for (i in id until length){
                val habit = newHabitList[i]
                val newHabit = HabitUiState(habit.id?.minus(1), habit.name, habit.description, habit.completed, habit.nextOccurring, habit.intervalDays)
                newHabitList[i] = newHabit
            }
            currentState.copy(
                habits = newHabitList.toList()
            )
        }
    }
}

typealias LogCallbackListHabits = (String) -> Unit