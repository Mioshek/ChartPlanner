package com.mioshek.chartplanner.views.settings

import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mioshek.chartplanner.assets.formats.DateFormatter
import com.mioshek.chartplanner.data.models.habits.CompletedRepository
import com.mioshek.chartplanner.data.models.habits.HabitsRepository
import com.mioshek.chartplanner.data.models.settings.Setting
import com.mioshek.chartplanner.data.models.settings.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val settings: List<Setting> = listOf()
)

class SettingsViewModel(
    savedStateHandle: SavedStateHandle,
    private val habitsRepository: HabitsRepository,
    private val completedRepository: CompletedRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {
    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    fun updateState(setting: Setting, newValue: String){
        val updatedSetting = Setting(
            id = setting.id,
            settingName = setting.settingName,
            value = newValue,
            visible = setting.visible
        )

        val newSettings = _settingsUiState.value.settings.toMutableList()
        newSettings[updatedSetting.id - 2] = updatedSetting

        _settingsUiState.update {currentState ->
            currentState.copy(
                settings = newSettings.toList()
            )
        }

        CoroutineScope(Dispatchers.Default).launch{
            settingsRepository.upsert(updatedSetting)
        }
    }

    suspend fun loadAllSettings(){
        val loadedSettings = settingsRepository.getAll().first().toMutableList()
        _settingsUiState.update { currentState ->
            currentState.copy(
                settings = loadedSettings
            )
        }
    }
}