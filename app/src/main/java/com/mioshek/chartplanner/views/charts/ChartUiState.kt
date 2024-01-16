package com.mioshek.chartplanner.views.charts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import com.mioshek.chartplanner.views.habits.ListHabitsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ChartUiState(
    val timestamp: CustomTimestamp,
    val maxDay: Int,
    val values: List<Float>
)

enum class CustomTimestamp{
    WEEK,
    MONTH,
    YEAR
}


class ChartViewModel(
    savedStateHandle: SavedStateHandle,
    private val habitsRepository: HabitsRepository
): ViewModel() {
    private val _listHabitsUiState = MutableStateFlow(ListHabitsUiState())
    val habitUiState: StateFlow<ListHabitsUiState> = _listHabitsUiState.asStateFlow()


}