@file:OptIn(ExperimentalMaterial3Api::class)

package com.mioshek.chartplanner.views.habits

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

data class HabitUiState(
    val id: Int? = null,
    val name: String = "Name",
    val description: String? = "Description",
    val completed: Boolean = false,
    val nextOccurring: Date? = null,
    val intervalDays: Int? = null, // 1 means everyday, null means once

)


class HabitViewModel: ViewModel() {
    private val _habitUiState = MutableStateFlow(HabitUiState())
    val habitUiState: StateFlow<HabitUiState> = _habitUiState.asStateFlow()

    private var logCallbackHabit: LogCallbackHabit? = null

    fun updateState(fieldId: Int, fieldValue: Any?){ //same usage as edit habit
        _habitUiState.update { currentState ->
            when(fieldId){
                2 -> {
                    currentState.copy(name = fieldValue.toString())
                }

                3 -> {
                    var actualValue = fieldValue
                    actualValue = if (fieldValue == null){
                        ""
                    } else{
                        actualValue.toString()
                    }
                    currentState.copy(description = actualValue)
                }

                5 -> {
                    currentState.copy(nextOccurring = fieldValue as Date?)
                }

                6 -> {
                    currentState.copy(intervalDays = fieldValue as Int?)
                }

                else -> {
                    Log.d("HabitUiStateError","FieldId: ${fieldId}, FieldValue: {$fieldValue}")
                    currentState.copy()
                }
            }
        }
    }
}

typealias LogCallbackHabit = (String) -> Unit