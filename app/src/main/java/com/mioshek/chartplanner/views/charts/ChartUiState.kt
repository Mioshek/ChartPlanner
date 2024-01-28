package com.mioshek.chartplanner.views.charts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mioshek.chartplanner.data.models.habits.Habit
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

data class ChartUiState(
    val timestamp: Timestamp = Timestamp.MONTH,
    val maxDay: Int = LocalDate.now().lengthOfMonth(),
    val values: List<Float> = listOf()
)

enum class Timestamp{
    WEEK,
    MONTH,
    YEAR
}
class CustomTimestamp(range: IntRange){
//    val WEEK = IntRange(1, 52)
//    val MONTH(IntRange(1, 12)),
//    YEAR(IntRange(2024, Calendar.getInstance().get(Calendar.YEAR)))
}


class ChartViewModel(
    savedStateHandle: SavedStateHandle,
    private val habitsRepository: HabitsRepository
): ViewModel() {
    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState: StateFlow<ChartUiState> = _chartUiState.asStateFlow()

    fun calculateValuesForChart(){
        for (day in 1.._chartUiState.value.maxDay){
        }
    }

//    fun doesHabitOccur(habit: Habit, givenDate: Long): Boolean {
//        val habitDate = LocalDate.ofEpochDay(habit.date/(24 * 60 * 60 * 1000))
//        val givenDate = LocalDate.ofEpochDay(givenDate/(24 * 60 * 60 * 1000))
//        val daysBetween = DAYS.between(habitDate, givenDate)
//        return daysBetween % habit.intervalDays == 0L
//    }
}