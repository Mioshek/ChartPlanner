package com.mioshek.chartplanner.views.habits

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mioshek.chartplanner.data.models.habits.CompletedRepository
import com.mioshek.chartplanner.data.models.habits.Habit
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import com.mioshek.chartplanner.data.models.settings.SettingsRepository
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
    val firstDate: Long? = null,
    val lastDate: Long? = null,
    val intervalDays: Int? = null, // 1 means everyday, 0 means once
    val selected: Boolean = false
)

fun HabitUiState.toHabit(): Habit {
    if (id == null){
        return Habit(
            name = name,
            description = description!!,
            firstDate =  firstDate!!,
            lastDate = lastDate ?: Long.MAX_VALUE,
            intervalDays = intervalDays!!
        )
    }
    return Habit(
        hid = id,
        name = name,
        description = description!!,
        firstDate = firstDate!!,
        lastDate = lastDate ?: Long.MAX_VALUE,
        intervalDays = intervalDays ?: 0
    )
}


class HabitViewModel(
    savedStateHandle: SavedStateHandle,
    private val habitsRepository: HabitsRepository,
    private val completedRepository: CompletedRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {
    private val _habitUiState = MutableStateFlow(HabitUiState())
    val habitUiState: StateFlow<HabitUiState> = _habitUiState.asStateFlow()

    private var logCallbackListHabits: LogCallbackHabit? = null

    fun updateState(fieldValue: Any?, fieldId: Int){ //same usage as edit habit
        _habitUiState.update { currentState ->
            var elementToUpdate = _habitUiState.asStateFlow().value

            var newName = elementToUpdate.name
            var newDescription = elementToUpdate.description
            var newCompleted = elementToUpdate.done
            var newFirstDate = elementToUpdate.firstDate
            var newLastDate = elementToUpdate.lastDate
            var newIntervalDays = elementToUpdate.intervalDays

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
                    newFirstDate = fieldValue as Long
                }

                6 ->{
                    newLastDate = fieldValue as Long
                }

                7 -> {
                    newIntervalDays  = fieldValue as Int?
                }

                else -> {
                    Log.d("HabitUiStateError","FieldId: ${fieldId}, FieldValue: {$fieldValue}")
                }
            }
            currentState.copy(
                name = newName,
                description = newDescription,
                done = newCompleted,
                firstDate = newFirstDate,
                lastDate = newLastDate,
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
                    firstDate = updatedHabit.firstDate,
                    lastDate = updatedHabit.lastDate,
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
        return _habitUiState.value.firstDate == null || _habitUiState.value.intervalDays == null
    }
}

typealias LogCallbackHabit = (String) -> Unit