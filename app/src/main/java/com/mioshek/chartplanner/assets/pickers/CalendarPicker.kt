package com.mioshek.chartplanner.assets.pickers

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import java.util.Calendar

/**
 * Modified [DatePickerDialog].
 * Displays Date starting first day current year up to 20 years further.
 * @return Start of selected day in UTC in seconds else current time in seconds (UTC timezone), Data Type: Long.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPicker(
    navController: NavController,
){
    val currentYear = Calendar.getInstance()[Calendar.YEAR]
    val datePickerState = rememberDatePickerState(
        yearRange = IntRange(currentYear, currentYear + 20)
    )

    DatePickerDialog(
        onDismissRequest = {navController.popBackStack()},
        confirmButton = {
            Button(onClick = {
                var date: Long = if (datePickerState.selectedDateMillis != null){
                    datePickerState.selectedDateMillis!!
                } else{
                    System.currentTimeMillis()
                }
                date /= 1000
                navController.previousBackStackEntry?.savedStateHandle?.set("pickedDate", date)
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