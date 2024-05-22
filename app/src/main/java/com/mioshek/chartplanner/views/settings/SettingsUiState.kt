package com.mioshek.chartplanner.views.settings

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.mioshek.chartplanner.assets.filesaver.ExportImport
import com.mioshek.chartplanner.data.models.habits.Completed
import com.mioshek.chartplanner.data.models.habits.CompletedRepository
import com.mioshek.chartplanner.data.models.habits.Habit
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
import kotlinx.coroutines.runBlocking


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
            displayType = setting.displayType,
            visible = setting.visible
        )

        val newSettings = _settingsUiState.value.settings.toMutableList()
        newSettings[updatedSetting.id - 1] = updatedSetting

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


    fun askForPermission(
        permission: String,
        context: Context,
        launcher: ManagedActivityResultLauncher<String, Boolean>,
    ): Boolean {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) -> {
                // Some works that require permission
                return true
            }
            else -> {
                // Asking for permission
                launcher.launch(permission)
                return ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    fun importAllData(activity: Context){

    }

    fun exportAllData(context: Context, navController: NavController){
        var habits: List<Habit>
        var completed: List<Completed>
        var settings: List<Setting>

        runBlocking {
            habits = habitsRepository.getAllHabitsStream().first()
            completed = completedRepository.getAllCompleted().first()
            settings = settingsRepository.getAll().first()
        }
        val data = arrayOf(habits, completed, settings)

        val jsonData = Gson().toJson(data)

        val intent = Intent(context, ExportImport::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("Option", CustomOption.EXPORT)
        intent.putExtra("Data", jsonData)
        ContextCompat.startActivity(context, intent, null)
    }
}

enum class CustomOption{
    EXPORT,
    IMPORT
}