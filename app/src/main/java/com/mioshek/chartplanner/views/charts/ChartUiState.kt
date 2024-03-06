package com.mioshek.chartplanner.views.charts

import android.util.Log
import androidx.annotation.InspectableProperty
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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

data class ChartUiState(
    val timestamp: CustomTimestamp = CustomTimestamp.MONTH,
    val numberOfDays: Int = LocalDate.now().lengthOfYear(),
    val firstDay: Int = (System.currentTimeMillis()/ 86400000).toInt() - numberOfDays, // 01.01.2024
    val yValues: MutableList<Float> = mutableListOf(),
)

enum class CustomTimestamp(val range: Int) {
    WEEK(7),
    MONTH(30),
    YEAR(355)
}

fun CustomTimestamp.getRange(): Int{
    return this.range
}


class ChartViewModel(
    savedStateHandle: SavedStateHandle,
    private val habitsRepository: HabitsRepository,
    private val completedRepository: CompletedRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {
    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState: StateFlow<ChartUiState> = _chartUiState.asStateFlow()

    fun changeTimestamp(timestamp: CustomTimestamp, firstDay: Int){
        _chartUiState.update {currentState ->
            currentState.copy(
                timestamp = timestamp,
                firstDay = firstDay,
                numberOfDays = timestamp.range
            )
        }
    }

    fun changeDays(add: Boolean){
        if (_chartUiState.value.firstDay - _chartUiState.value.numberOfDays < 19723 && !add) return
        _chartUiState.update {currentState ->
            val newFirstDay = if (add) currentState.firstDay + currentState.numberOfDays else currentState.firstDay - currentState.numberOfDays
            currentState.copy(
                firstDay = newFirstDay
            )
        }
    }

    suspend fun calculateValuesForChart(): Boolean {
        val start = _chartUiState.value.firstDay
        val yValues = completedRepository.getChartStatistics(start,_chartUiState.value.numberOfDays).toMutableList()
        Log.d("Time taken", "${(Instant.now().toEpochMilli() - start /86400)/1000f} s")
        _chartUiState.update { currentState ->
            currentState.copy(
                timestamp = currentState.timestamp,
                numberOfDays = currentState.numberOfDays,
                yValues = yValues
            )
        }
        return false
    }

    suspend fun getSettingValue(name: String): String {
        return settingsRepository.getSetting(name).first().value
    }
}