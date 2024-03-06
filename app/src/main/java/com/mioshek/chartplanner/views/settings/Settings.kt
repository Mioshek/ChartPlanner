package com.mioshek.chartplanner.views.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mioshek.chartplanner.data.models.settings.Setting
import com.mioshek.chartplanner.ui.AppViewModelProvider

@Composable
fun Settings(
    navController: NavController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val settingsUiState by settingsViewModel.settingsUiState.collectAsState()

    val scrollState = rememberScrollState(0)
    LaunchedEffect(key1 = "test") {
        settingsViewModel.loadAllSettings()
    }

    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        for (setting in settingsUiState.settings){
            SettingContainer(setting, settingsViewModel)
        }
    }
}


@Composable
fun SettingContainer(
    setting: Setting,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
){
    Card(
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(10.dp)
        ) {

            Text(
                text = setting.settingName,
                modifier = modifier.weight(1f)
            )

            if (setting.value.toIntOrNull() == null){

                Switch(
                    checked = setting.value.toBoolean(),
                    onCheckedChange = {
                        val invertedValue = !setting.value.toBoolean()
                        settingsViewModel.updateState(setting, invertedValue.toString())
                    }
                )
            }
            else{
                Slider(
                    value = setting.value.toFloat(),
                    onValueChange = {
                        settingsViewModel.updateState(setting, it.toInt().toString())
                    },
                    valueRange = 10f..30f,
                    steps = 20,
                    modifier = modifier.weight(2f)
                )
                Text(text = setting.value + " Px")
            }
        }
    }
}