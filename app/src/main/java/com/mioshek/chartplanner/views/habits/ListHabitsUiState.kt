package com.mioshek.chartplanner.views.habits


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mioshek.chartplanner.data.models.habits.Completed
import com.mioshek.chartplanner.data.models.habits.CompletedRepository
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import com.mioshek.chartplanner.data.models.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class ListHabitsUiState(
    val habits: List<HabitUiState> = listOf(),
    val enterSelectMode: Boolean = false
)

class ListHabitsViewModel(
    savedStateHandle: SavedStateHandle,
    private val habitsRepository: HabitsRepository,
    private val completedRepository: CompletedRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {
    private val _listHabitsUiState = MutableStateFlow(ListHabitsUiState())
    val habitUiState: StateFlow<ListHabitsUiState> = _listHabitsUiState.asStateFlow()

    private var logCallbackListHabits: LogCallbackListHabits? = null

    @Composable
    fun UpdateListUi(date: Long){
        val extractedHabits = habitsRepository.getAllHabitsByDateStream(date).collectAsState(null).value
        val extractedCompleted = completedRepository.getByDate(date).collectAsState(null).value
        if (extractedHabits != null){
            val readyHabits = mutableListOf<HabitUiState>()
            for (habit in extractedHabits){
                var done = false
                if (extractedCompleted != null){
                    for (completed in extractedCompleted){
                        if(habit.hid == completed.habitId){
                            done = true
                        }
                    }
                }

                readyHabits.add(
                    HabitUiState(
                        habit.hid, habit.name, habit.description, done, habit.date, habit.intervalDays
                    )
                )
            }

            _listHabitsUiState.update { currentState->
                currentState.copy(
                    habits = readyHabits
                )
            }
        }
    }

    fun changeSelection(selection: Boolean){
        _listHabitsUiState.update {currentState ->
            currentState.copy(enterSelectMode = selection)
        }
    }

    suspend fun completeHabit(hid: Int, chosenDate: Long){
        completedRepository.insert(
            Completed(
                habitId = hid,
                date = chosenDate
            )
        )
    }

    suspend fun tickUndoneHabit( hid: Int, date: Long){
        completedRepository.delete(date, hid)
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