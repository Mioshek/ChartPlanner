package com.mioshek.chartplanner.assets.pickers

import android.annotation.SuppressLint
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar

@SuppressLint("UnrememberedGetBackStackEntry", "SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPicker(
    navController: NavController,
){
    val currentYear = Calendar.getInstance()[Calendar.YEAR]
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val datePickerState = rememberDatePickerState(
        yearRange = IntRange(currentYear, currentYear + 20)
    )

    DatePickerDialog(
        onDismissRequest = {navController.popBackStack()},
        confirmButton = {
            Button(onClick = {
                val date: Long = if (datePickerState.selectedDateMillis != null){
                    datePickerState.selectedDateMillis!!
                } else{
                    System.currentTimeMillis()
                }

                navController.previousBackStackEntry?.savedStateHandle?.set("date", date)
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