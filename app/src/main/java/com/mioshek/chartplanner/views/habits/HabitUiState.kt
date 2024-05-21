package com.mioshek.chartplanner.views.habits

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
    val description: String? = null,
    val done: Boolean = false,
    val startEpochDate: Int? = null,
    val startEpochTime: Int? = null,
    val endEpochDate: Int? = null,
    val endEpochTime: Int? = null,
    val intervalDays: Int = 0, // 1 means everyday, 0 means once
)

fun HabitUiState.toHabit(): Habit {
    if (id == null){
        return Habit(
            name = name,
            description = description,
            startEpochDate =  startEpochDate!!,
            startEpochTime =  startEpochTime!!,
            endEpochDate = endEpochDate,
            endEpochTime = endEpochTime,
            intervalDays = intervalDays
        )
    }
    return Habit(
        hId = id,
        name = name,
        description = description,
        startEpochDate =  startEpochDate!!,
        startEpochTime =  startEpochTime!!,
        endEpochDate = endEpochDate,
        endEpochTime = endEpochTime,
        intervalDays = intervalDays
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

    /**
     * [fieldId] A list of options to configure the action. Supported options include:
     *  1. [HabitUiState.id] : Int? Is given at the beginning of creating a habit and cannot be changed!
     *  1. [HabitUiState.name] : String
     *  1. [HabitUiState.description] : String?
     *  1. [HabitUiState.done] : Boolean
     *  1. [HabitUiState.startEpochDate] : Int?
     *  1. [HabitUiState.startEpochTime] : Int?
     *  1. [HabitUiState.endEpochDate] : Int?
     *  1. [HabitUiState.endEpochTime] : Int?
     *  1. [HabitUiState.intervalDays] : Int?
     *  @param fieldValue type -> Any?
     */
    fun updateState(fieldValue: Any?, fieldId: Int){
        _habitUiState.update { currentState ->

            when(fieldId){
                2 -> {
                    currentState.copy(name = fieldValue as String)
                }

                3 -> {
                    currentState.copy(description = fieldValue as String?)
                }

                4 -> {
                    currentState.copy(done = fieldValue as Boolean)
                }

                5 -> {
                    currentState.copy(startEpochDate = fieldValue as Int)
                }

                6 ->{
                    currentState.copy(startEpochTime = fieldValue as Int)
                }

                7 -> {
                    currentState.copy(endEpochDate = fieldValue as Int)
                }

                8 -> {
                    currentState.copy(endEpochTime = fieldValue as Int?)
                }

                9 -> {
                    currentState.copy(intervalDays = fieldValue as Int)
                }

                else -> {
                    throw IllegalArgumentException("FieldId: ${fieldId}, FieldValue: {$fieldValue}")
                }
            }
        }
    }

    suspend fun getHabitFromDb(id: Int){
        val updatedHabit = habitsRepository.getHabitStream(id).first()
        if (updatedHabit != null) {
            _habitUiState.update {currentState ->
                currentState.copy(
                    id = updatedHabit.hId,
                    name = updatedHabit.name,
                    description = updatedHabit.description,
                    startEpochDate = updatedHabit.startEpochDate,
                    startEpochTime = updatedHabit.startEpochTime,
                    endEpochDate = updatedHabit.endEpochDate,
                    endEpochTime = updatedHabit.endEpochTime,
                    intervalDays = updatedHabit.intervalDays
                )
            }
        }
    }

    fun timeToMinutes(time: String): Int {
        val parts = time.split(":")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        return hours * 60 + minutes
    }

    fun minutesToTime(): String {
        val hours = _habitUiState.value.startEpochTime?.div(60)
        val mins = _habitUiState.value.startEpochTime?.rem(60)
        return "%02d:%02d".format(hours, mins)
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
        return _habitUiState.value.startEpochDate == null || _habitUiState.value.name == ""
    }
}

typealias LogCallbackHabit = (String) -> Unit