package com.mioshek.chartplanner.views.charts

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mioshek.chartplanner.assets.formats.DateFormatter
import com.mioshek.chartplanner.data.models.habits.CompletedRepository
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import com.mioshek.chartplanner.data.models.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.LocalDate
import java.util.Calendar

data class ChartUiState(
    val timestamp: Int = Calendar.MONTH,
    val maxDay: Int = LocalDate.now().lengthOfYear(),
    val yValues: MutableList<Float> = mutableListOf()
)

enum class CustomTimestamp(val range: IntRange) {
    WEEK(1..52),
    MONTH(1..12),
    YEAR(2024..Calendar.getInstance().get(Calendar.YEAR))
}


class ChartViewModel(
    savedStateHandle: SavedStateHandle,
    private val habitsRepository: HabitsRepository,
    private val completedRepository: CompletedRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {
    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState: StateFlow<ChartUiState> = _chartUiState.asStateFlow()

    fun changeTimestamp(timestamp: Calendar){
        _chartUiState.update {currentState ->
            currentState.copy(

            )
        }
    }

    suspend fun calculateValuesForChart(): Boolean {
        val yValues = mutableListOf<Float>()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2024)
        // If the month is set to current month then it iterates from the end of this month. For example 2 -> from 01.03.YYYY
//        calendar.set(Calendar.MONTH, _chartUiState.value.timestamp -1)
        val start = Instant.now().toEpochMilli()
        for (day in 1.._chartUiState.value.maxDay){
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val formattedDay = DateFormatter.sdf.format(calendar.timeInMillis).substring(0,10)
//            Log.d("Day", formattedDay)
            yValues.add(calculateDay(formattedDay))
        }
        Log.d("Time taken", "${(Instant.now().toEpochMilli() - start)/1000f} s")
        _chartUiState.update { currentState ->
            currentState.copy(
                timestamp = currentState.timestamp,
                maxDay = currentState.maxDay,
                yValues = yValues
            )
        }
        return false
    }

    private suspend fun calculateDay(day: String): Float {
        val allDayHabits = habitsRepository.getAllHabitsByDateStream(day).first().size
        val extractedCompleted = completedRepository.getByDate(day).first().size
        return if (allDayHabits > 0 && extractedCompleted > 0){
            extractedCompleted / allDayHabits.toFloat() * 100
        } else 0f
    }
}