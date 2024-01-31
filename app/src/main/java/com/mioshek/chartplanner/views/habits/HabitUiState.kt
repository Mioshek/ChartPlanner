package com.mioshek.chartplanner.views.habits

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mioshek.chartplanner.assets.formats.DateFormatter
import com.mioshek.chartplanner.data.models.habits.Completed
import com.mioshek.chartplanner.data.models.habits.CompletedRepository
import com.mioshek.chartplanner.data.models.habits.Habit
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

data class HabitUiState(
    val id: Int? = null,
    val name: String = "",
    val description: String? = "",
    val done: Boolean = false,
    val date: String? = null,
    val intervalDays: Int? = null, // 1 means everyday, 0 means once
    val selected: Boolean = false
)

fun HabitUiState.toHabit(): Habit {
    if (id == null){
        return Habit(
            name = name,
            description = description!!,
            date =  date!!,
            intervalDays = intervalDays!!
        )
    }
    return Habit(
        hid = id,
        name = name,
        description = description!!,
        date =  date!!,
        intervalDays = intervalDays ?: 0
    )
}


class HabitViewModel(
    savedStateHandle: SavedStateHandle,
    private val habitsRepository: HabitsRepository,
    private val completedRepository: CompletedRepository
): ViewModel() {
    private val _habitUiState = MutableStateFlow(HabitUiState())
    val habitUiState: StateFlow<HabitUiState> = _habitUiState.asStateFlow()

    private var logCallbackListHabits: LogCallbackHabit? = null

    fun updateState(fieldValue: Any?, fieldId: Int){ //same usage as edit habit
        _habitUiState.update { currentState ->
            var elementToUpdate = _habitUiState.asStateFlow().value

            var newName = elementToUpdate?.name
            var newDescription = elementToUpdate?.description
            var newCompleted = elementToUpdate?.done
            var newDate = elementToUpdate?.date
            var newIntervalDays = elementToUpdate?.intervalDays

            when(fieldId){
                2 -> {
                    newName= fieldValue as String
                }

                3 -> {
                    var actualValue = fieldValue
                    actualValue = if (fieldValue == null){
                        ""
                    } else{
                        actualValue.toString()
                    }
                    newDescription = actualValue
                }

                4 -> {
                    newCompleted = fieldValue as Boolean
                }

                5 -> {
                    newDate = fieldValue as String
                }

                6 -> {
                    newIntervalDays  = fieldValue as Int?
                }

                else -> {
                    Log.d("HabitUiStateError","FieldId: ${fieldId}, FieldValue: {$fieldValue}")
                }
            }
            currentState.copy(
                name = newName.toString(),
                description = newDescription,
                done = newCompleted!!,
                date = newDate,
                intervalDays = newIntervalDays!!
            )
        }
    }

    suspend fun getHabitFromDb(id: Int){
        val updatedHabit = habitsRepository.getHabitStream(id).first()
        if (updatedHabit != null) {
            _habitUiState.update {currentState ->
                currentState.copy(
                    id = updatedHabit.hid,
                    name = updatedHabit.name,
                    description = updatedHabit.description,
                    date = updatedHabit.date,
                    intervalDays = updatedHabit.intervalDays
                )
            }
        }
    }

    suspend fun insertHabitDb(newHabit: Habit){
        habitsRepository.insert(newHabit)
    }

    suspend fun updateHabitDb(newHabit: Habit){
        habitsRepository.update(newHabit)
    }

    suspend fun getHabitById(id: Int): Habit? {
        return habitsRepository.getHabitStream(id).first()
    }


    fun checkIfAnyNull(): Boolean {
        if(
            _habitUiState.value.name == null ||
            _habitUiState.value.date == null ||
            _habitUiState.value.intervalDays == null
        ){
            return true
        }
        return false
    }
}

typealias LogCallbackHabit = (String) -> Unit