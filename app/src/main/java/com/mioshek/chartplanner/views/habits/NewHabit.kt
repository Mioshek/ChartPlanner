package com.mioshek.chartplanner.views.habits

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mioshek.chartplanner.R
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme
import java.util.Calendar
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewHabit(
    navController: NavController,
    habitViewModel: HabitViewModel = viewModel(),
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ){
        val options = listOf("Don't Repeat",1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30)
        var expanded by remember { mutableStateOf(false) }
        val newHabitUiState by habitViewModel.habitUiState.collectAsState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(start = 75.dp, end = 75.dp)
        ){

            OutlinedTextField(
                newHabitUiState.name,
                onValueChange = {habitViewModel.updateState(2,it)},
                modifier = modifier.padding(bottom = 10.dp)
            )

            Row(
                modifier = modifier.fillMaxWidth()
            ){
                var displayedNextOccurring = if (newHabitUiState.nextOccurring == null) "Starting Date" else "${newHabitUiState.nextOccurring!!.day} ${newHabitUiState.nextOccurring!!.month} ${newHabitUiState.nextOccurring!!.year}"
                OutlinedTextField(
                    displayedNextOccurring,
                    onValueChange = {
                                    Log.d("OnValueChange", it)
                    },
                    readOnly = true,
                    modifier = modifier
                        .padding(bottom = 10.dp, end = 10.dp)
                        .fillMaxWidth()
                        .weight(3f),
                )
                Button(
                    onClick = {
                        navController.navigate("habits/new/calendar")
                    },
                    modifier.fillMaxWidth().weight(1f)
                ){
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.calendar_check),
                        contentDescription = "Date Picker",
                    )
                }
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {expanded = !expanded},
                modifier = modifier.padding(bottom = 10.dp)
            ){
                var intervalDaysDisplayed = newHabitUiState.intervalDays.toString()
                if (intervalDaysDisplayed == "null"){
                    intervalDaysDisplayed = "Don't repeat"
                }
                OutlinedTextField(
                    value = intervalDaysDisplayed,
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    onValueChange = {},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption.toString()) },
                            onClick = {
                                if (selectionOption == "Don't Repeat"){
                                    habitViewModel.updateState(6, null)
                                }
                                else{
                                    Log.d("help", selectionOption.toString())
                                    habitViewModel.updateState(6, selectionOption)
                                }
                                expanded = false
                            },
                        )
                    }
                }
            }

            OutlinedTextField(
                newHabitUiState.description.toString(),
                onValueChange = {habitViewModel.updateState(3, it)},
                modifier = modifier.padding(bottom = 10.dp),
                singleLine = false,
                minLines = 5,
                maxLines = 10
            )

            Row(
            ){
                Button(
                    onClick = {}
                ){
                    Text("Save")
                }

                Spacer(
                    modifier = modifier.size(10.dp)
                )

                Button(
                    onClick = {
                        navController.popBackStack()
                    }
                ){
                    Text("Cancel")
                }
            }
        }
    }
}

@SuppressLint("UnrememberedGetBackStackEntry")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPicker(
    navController: NavController,
    habitViewModel: HabitViewModel = viewModel()
){
    val currentYear = Calendar.getInstance()[Calendar.YEAR]
    val datePickerState = rememberDatePickerState(
        yearRange = IntRange(currentYear, currentYear + 20)
    )

    DatePickerDialog(
        onDismissRequest = {navController.popBackStack()},
        confirmButton = {
            Button(onClick = {
                var longDate = 1L
                if (datePickerState.selectedDateMillis != null){
                    longDate = datePickerState.selectedDateMillis!!
                }
                val date = Date(longDate)
                habitViewModel.updateState(5, date)
                navController.popBackStack()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                navController.popBackStack()
            }) {
                Text(text = "Cancel")
            }
        }
    ){
        DatePicker(
            state = datePickerState
        )
    }
}
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = "id:pixel_6_pro")
@Composable
fun NewHabitPreview(){
    ChartPlannerTheme {
        NewHabit(rememberNavController())
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = "id:pixel_6_pro")
@Composable
fun CalendarPickerPreview(){
    CalendarPicker(rememberNavController())
}