package com.mioshek.chartplanner.views.habits


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mioshek.chartplanner.R
import com.mioshek.chartplanner.assets.formats.DateFormatter
import com.mioshek.chartplanner.data.models.habits.Completed
import com.mioshek.chartplanner.data.models.habits.CompletedRepository
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import com.mioshek.chartplanner.data.models.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update


data class ListHabitsUiState(
    val habits: List<HabitUiState> = listOf(),
    val enterSelectMode: Boolean = false,
    val selectedHabits: MutableList<HabitUiState> = mutableListOf()
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
    fun UpdateListUi(date: Int){
        val extractedHabits = habitsRepository.getAllHabitsByDateStream(date).collectAsState(null).value
        val extractedCompleted = completedRepository.getByDate(date).collectAsState(null).value
        if (extractedHabits != null){
            val readyHabits = mutableListOf<HabitUiState>()
            for (habit in extractedHabits){
                var done = false
                if (extractedCompleted != null){
                    for (completed in extractedCompleted){
                        if(habit.hId == completed.hId){
                            done = true
                        }
                    }
                }

                readyHabits.add(
                    HabitUiState(
                        habit.hId,
                        habit.name,
                        habit.description,
                        done,
                        habit.startEpochDate,
                        habit.startEpochTime,
                        habit.endEpochDate,
                        habit.endEpochTime,
                        habit.intervalDays
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

    fun selectHabit(habit: HabitUiState){
        val newSelectedHabits = _listHabitsUiState.value.selectedHabits
        newSelectedHabits.add(habit)
        _listHabitsUiState.update {currentState ->
            currentState.copy(
                selectedHabits = newSelectedHabits
            )
        }
    }

    fun unselectHabit(habit: HabitUiState){
        val newSelectedHabits = _listHabitsUiState.value.selectedHabits
        newSelectedHabits.remove(habit)
        _listHabitsUiState.update {currentState ->
            currentState.copy(
                selectedHabits = newSelectedHabits
            )
        }
    }

    fun isHabitSelected(habit: HabitUiState): Boolean {
        return habit in _listHabitsUiState.value.selectedHabits
    }

    suspend fun changeTickState(hid: Int, date: Int, time: Int, previousState: Boolean){
        val completeAnytime = settingsRepository.getSetting(R.string.allow_completing_habits_anytime).first().value.toBoolean()
        val today = DateFormatter.currentDate
        if (completeAnytime || date == today){

            if (previousState){
                completedRepository.delete(hid, date, time)// add time
            }
            else{
                completedRepository.insert(
                    Completed(
                        hId = hid,
                        date = date,
                        time = time
                    )
                )
            }
        }
    }

    suspend fun removeSelectedHabits(){
        for (habit in _listHabitsUiState.value.selectedHabits){
            habitsRepository.delete(habit.id!!)
        }
    }
}

typealias LogCallbackListHabits = (String) -> Unit