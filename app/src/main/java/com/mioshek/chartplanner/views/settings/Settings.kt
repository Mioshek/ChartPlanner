package com.mioshek.chartplanner.views.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.mioshek.chartplanner.R
import com.mioshek.chartplanner.assets.formats.Languages
import com.mioshek.chartplanner.data.models.settings.Setting
import com.mioshek.chartplanner.ui.AppViewModelProvider

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Settings(
    navController: NavController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val context = LocalContext.current
    val settingsUiState by settingsViewModel.settingsUiState.collectAsState()
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

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

        Row (
            verticalAlignment = Alignment.Bottom,
            modifier = modifier.padding(10.dp)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .weight(1f)
                    .clickable {
                        if (
                            settingsViewModel.askForPermission(
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                context,
                                launcher,
                            )
                        ) {
                            settingsViewModel.exportAllData(context, navController)

                        }
                    }
            ) {
                Icon(painter = painterResource(id = R.drawable.export_db), contentDescription = "Export")
                Text(text = "Export")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .weight(1f)
                    .clickable {
                    }
            ) {
                Icon(painter = painterResource(id = R.drawable.import_db), contentDescription = "Import")
                Text(text = "Import")
            }
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
        var expanded by remember{ mutableStateOf(false)}

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(10.dp)
                .clickable { expanded = !expanded }
        ) {

            Text(
                text = setting.settingName,
                modifier = modifier.weight(1f)
            )

            when (setting.displayType){
                1 -> {
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

                2 ->{
                    Switch(
                        checked = setting.value.toBoolean(),
                        onCheckedChange = {
                            val invertedValue = !setting.value.toBoolean()
                            settingsViewModel.updateState(setting, invertedValue.toString())
                        }
                    )
                }

                3 ->{
                    var chosenLanguage by remember{ mutableStateOf(0)} // EN
                    val screenHeight = LocalDensity.current.density * LocalView.current.height
                    val scrollState = rememberScrollState(0)
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = !expanded},
                        modifier = modifier
                            .verticalScroll(scrollState)
                            .sizeIn(maxHeight = (screenHeight * 0.03f).dp)
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight()
                    ) {
                        val languages = Languages.languages

                        for ((index, language) in languages.withIndex()){
                            DropdownMenuItem(
                                text = { Text(text = language, fontSize = 25.sp)},
                                onClick = {
                                    expanded = !expanded
                                        chosenLanguage = index
                                    },
                                modifier = modifier.fillMaxSize()
                            )

                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f) // Set the width to 90% of the available width
                                    .align(Alignment.CenterHorizontally),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Text(
                        text = Languages.languages[chosenLanguage],
                        modifier = modifier.weight(0.5f)
                    )

                    val arrowIcon = if (expanded) {
                        Icons.Rounded.KeyboardArrowDown
                    } else {
                        Icons.Rounded.KeyboardArrowUp
                    }

                    Icon(
                        imageVector = arrowIcon,
                        contentDescription = "Dropdown",
                        modifier = modifier.weight(0.2f)
                    )
                }
            }
        }
    }
}