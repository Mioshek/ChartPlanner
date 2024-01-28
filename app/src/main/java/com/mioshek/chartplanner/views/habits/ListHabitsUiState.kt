package com.mioshek.chartplanner.views.habits


import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mioshek.chartplanner.data.models.habits.Habit
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update


data class ListHabitsUiState(
    val habits: Flow<List<Habit>> = flowOf(listOf()),
    val enterSelectMode: Boolean = false
)

class ListHabitsViewModel(
    savedStateHandle: SavedStateHandle,
    private val habitsRepository: HabitsRepository
): ViewModel() {
    private val _listHabitsUiState = MutableStateFlow(ListHabitsUiState())
    val habitUiState: StateFlow<ListHabitsUiState> = _listHabitsUiState.asStateFlow()

    private var logCallbackListHabits: LogCallbackListHabits? = null


    @Composable
    fun updateListUi(date: String){
        val extracted = habitsRepository.getAllHabitsByDateStream(date)

        _listHabitsUiState.update { currentState->
            currentState.copy(
                habits = extracted
            )
        }
    }

    fun changeSelection(selection: Boolean){
        _listHabitsUiState.update {currentState ->
            currentState.copy(enterSelectMode = selection)
        }
    }

//    fun removeHabit(id: Int){
//        _listHabitsUiState.update { currentState ->
//            val newHabitList = currentState.habits.toMutableList()
//            newHabitList.removeAt(id)
//            val length = newHabitList.size
//            for (i in id until length){
//                val habit = newHabitList[i]
//                val newHabit = HabitUiState(habit.id?.minus(1), habit.name, habit.description, habit.completed, habit.date, habit.intervalDays)
//                newHabitList[i] = newHabit
//            }
//            currentState.copy(
//                habits = newHabitList.toList()
//            )
//        }
//    }
}

typealias LogCallbackListHabits = (String) -> Unit