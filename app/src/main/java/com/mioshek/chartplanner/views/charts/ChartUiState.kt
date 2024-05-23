package com.mioshek.chartplanner.views.charts

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
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

data class ChartUiState(
    val timestamp: CustomTimestamp = CustomTimestamp.MONTH,
    val firstDay: Int = if (((System.currentTimeMillis() + DateFormatter.timezoneOffset)/ 86400000).toInt() -
        LocalDate.now().lengthOfYear() <= 19723) 19723 else (System.currentTimeMillis() + DateFormatter.timezoneOffset).toInt(),
    val numberOfDays: Int = ((System.currentTimeMillis() + DateFormatter.timezoneOffset)/ 86400000).toInt() - firstDay,
    val yValues: MutableList<Float> = mutableListOf(),
)

enum class CustomTimestamp(val range: Int) {
    WEEK(7),
    MONTH(30),
    YEAR(365)
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

    fun changeTimestamp(timestamp: CustomTimestamp){
        _chartUiState.update {currentState ->
            currentState.copy(
                timestamp = timestamp,
                numberOfDays = timestamp.range
            )
        }

        runBlocking {
            launch {
                calculateValuesForChart()
            }
        }
    }

    fun changeDays(add: Boolean){
        var newFirstDay: Int
        _chartUiState.update {currentState ->
            if (_chartUiState.value.firstDay - _chartUiState.value.numberOfDays < 19723 && !add){
                newFirstDay = 19723
            }
            else{
                newFirstDay = if (add) currentState.firstDay + currentState.numberOfDays else currentState.firstDay - currentState.numberOfDays
            }
            currentState.copy(
                firstDay = newFirstDay
            )
        }

        runBlocking {
            launch {
                calculateValuesForChart()
            }
        }
    }

    suspend fun calculateValuesForChart(): Boolean {
        val start = _chartUiState.value.firstDay
        val yValues = completedRepository.getChartStatistics(start,_chartUiState.value.numberOfDays).toMutableList()
        _chartUiState.update { currentState ->
            currentState.copy(
                timestamp = currentState.timestamp,
                numberOfDays = currentState.numberOfDays,
                yValues = yValues
            )
        }
        return false
    }

    suspend fun getSettingValue(name: Int): String {
        return settingsRepository.getSetting(name).first().value
    }
}