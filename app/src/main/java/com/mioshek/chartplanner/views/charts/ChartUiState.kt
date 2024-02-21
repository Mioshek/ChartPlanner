package com.mioshek.chartplanner.views.charts

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mioshek.chartplanner.assets.formats.DateFormatter
import com.mioshek.chartplanner.data.models.habits.CompletedRepository
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.util.Calendar

data class ChartUiState(
    val timestamp: Timestamp = Timestamp.YEAR,
    val maxDay: Int = LocalDate.now().lengthOfYear(),
    val yValues: MutableList<Float> = mutableListOf()
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
    private val habitsRepository: HabitsRepository,
    private val completedRepository: CompletedRepository
): ViewModel() {
    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState: StateFlow<ChartUiState> = _chartUiState.asStateFlow()

    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun CalculateValuesForChart(){
        _chartUiState.value.yValues.clear()
        for (day in 1.._chartUiState.value.maxDay){
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, 2024)
            calendar.set(Calendar.DAY_OF_YEAR, day)
            val formattedDay = DateFormatter.sdf.format(calendar.timeInMillis).substring(0,10)
            _chartUiState.value.yValues.add(calculateDay(formattedDay))
        }
    }

    @Composable
    fun calculateDay(day: String): Float {
        val allDayHabits = habitsRepository.getAllHabitsByDateStream(day).collectAsState(null).value?.size
        val extractedCompleted = completedRepository.getByDate(day).collectAsState(null).value
        return if (allDayHabits != null && extractedCompleted != null){
            extractedCompleted.size / allDayHabits.toFloat() * 100
        } else 0f
    }
}