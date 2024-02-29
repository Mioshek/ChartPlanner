package com.mioshek.chartplanner.views.habits

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.rememberNavController
import com.mioshek.chartplanner.R
import com.mioshek.chartplanner.assets.formats.DateFormatter
import com.mioshek.chartplanner.assets.pickers.CalendarPicker
import com.mioshek.chartplanner.assets.pickers.CustomNumberPicker
import com.mioshek.chartplanner.assets.pickers.rememberPickerState
import com.mioshek.chartplanner.ui.AppViewModelProvider
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun NewHabit(
    navController: NavController,
    habitId: Int?,
    habitViewModel: HabitViewModel = viewModel(factory = AppViewModelProvider.Factory),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
){
    val coroutineScope = rememberCoroutineScope()
    val habit = habitViewModel.habitUiState.collectAsState()
    var updatedFromDb = navController.previousBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("isInsertedToDb")?.value ?: true
    val focusManager = LocalFocusManager.current
    val source = remember{ MutableInteractionSource()}
    var isSourcePressed by remember { mutableStateOf(false) }// second argument -> was clicked?

    if (habitId != null && !updatedFromDb){
        coroutineScope.launch {
            habitViewModel.getHabitFromDb(habitId)
        }
        navController.previousBackStackEntry?.savedStateHandle?.set("isInsertedToDb", true)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                focusManager.clearFocus()
            }
    ){
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                .padding(8.dp)
                .align(Alignment.TopStart)
                .offset(20.dp, 20.dp)
                .clickable {
                    navController.popBackStack()
                }
        ){
            Icon(
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = "Go Back",
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp)
        ){
            OutlinedTextField(
                habit.value.name,
                placeholder = {Text("Name")},
                onValueChange = {habitViewModel.updateState(it, 2)},
                modifier = modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
            )

            // Changes in date listener
            LaunchedEffect(key1 = Unit){
                val longDate = navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Long?>("date")?.value
                if (longDate != null && habit.value.date != longDate){
                    habitViewModel.updateState(
                        longDate,
                        5
                    )
                }
            }

            Row(
                modifier = modifier.fillMaxWidth()
            ){
                OutlinedTextField(
                    value = habit.value.date?.let { DateFormatter.sdf.format(it * 1000).substring(0, 10) } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    modifier = modifier
                        .padding(bottom = 10.dp, end = 10.dp)
                        .fillMaxWidth()
                        .weight(3f),
                    placeholder = { Text("Starting Date") },
                    interactionSource = source
                )
                if (source.collectIsPressedAsState().value && !isSourcePressed && navController.currentDestination != NavDestination("habits/new/calendar")){
                    isSourcePressed = true
                    navController.navigate(route = "habits/new/calendar")
                }

                Button(
                    onClick = {
                        navController.navigate(route = "habits/new/calendar")
                    },
                    modifier.fillMaxWidth().weight(1f).align(Alignment.CenterVertically),
                ){
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.calendar_check),
                        contentDescription = "Date Picker",
                    )
                }
            }

            if (habitId == null){
                NumberPicker(habitViewModel)
            }
            else if (habit.value.intervalDays != null){
                NumberPicker(habitViewModel)
            }


            OutlinedTextField(
                habit.value.description.toString(),
                onValueChange = {
                    habitViewModel.updateState(it, 3)},
                placeholder = {Text("Description")},
                modifier = modifier.padding(bottom = 10.dp).fillMaxWidth(),
                singleLine = false,
                minLines = 5,
                maxLines = 10
            )
            NavigationButtons(habitViewModel, habitId, habit, coroutineScope, navController)
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NumberPicker(
    habitViewModel: HabitViewModel,
    modifier: Modifier = Modifier
){
    val options = listOf("Don't Repeat",1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30)

    val valuesPickerState = rememberPickerState()
    val stringOptions = options.map { it.toString() }
    Column(
        modifier = modifier.padding(bottom = 20.dp, top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Chose Day Interval")

        Row {
            CustomNumberPicker(
                valuesPickerState,
                stringOptions,
                3,
                MaterialTheme.colorScheme.secondary,
                habitViewModel.habitUiState.value.intervalDays ?: 0,
                onValueChange = {
                    if (it == "Don't Repeat"){
                        habitViewModel.updateState(0, 6)
                    }
                    else{
                        habitViewModel.updateState(it.toInt(), 6)
                    }
                },
                fontSize = 20.sp,
                padding = 10.dp
            )
        }

        Text(
            when(habitViewModel.habitUiState.value.intervalDays){
                1 -> "Repeat Everyday"
                0 -> "Don't Repeat"
                else -> "Repeat Every ${habitViewModel.habitUiState.value.intervalDays} Days"
            }
        )
    }
}

@Composable
fun NavigationButtons(
    habitViewModel: HabitViewModel,
    habitId: Int?,
    habit: State<HabitUiState>,
    coroutineScope: CoroutineScope,
    navController: NavController,
    modifier: Modifier = Modifier
){
    var displayAllert by remember { mutableStateOf(false) }
    if (displayAllert){
        AlertDialog(
            icon = {
                Icon(painterResource(R.drawable.error), contentDescription = "Example Icon")
            },
            title = {
                Text(text = "Empty Field")
            },
            text = {
                Text(text = "One or more fields are empty and need to be filled. Please ensure all required information is provided before proceeding.")
            },
            onDismissRequest = {
                displayAllert = false
            },
            confirmButton = {
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        displayAllert = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }

    Row {
        Button(
            onClick = {
                navController.popBackStack()
            }
        ){
            Text("Cancel")
        }

        Spacer(
            modifier = modifier.size(10.dp)
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    if (habitViewModel.checkIfAnyNull()){
                        displayAllert = true
                    }
                    else{
                        if (habitId == null ){
                            habitViewModel.insertHabitDb(habit.value.toHabit())
                        }
                        else{
                            habitViewModel.updateHabitDb(habit.value.toHabit())
                        }
                        navController.popBackStack()
                    }
                }
            }
        ){
            Text("Save")
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = "id:pixel_6_pro")
@Composable
fun NewHabitPreview(){
    ChartPlannerTheme {
//        NewHabit(rememberNavController(), 1, false)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = "id:pixel_6_pro")
@Composable
fun CalendarPickerPreview(){
    CalendarPicker(rememberNavController())
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = "id:pixel_6_pro")
@Composable
fun TimePickerPreview(){
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedTimeText by remember { mutableStateOf("") }

// Fetching current hour, and minute
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    Box {
        val timePicker = TimePickerDialog(
            context,
            { _, selectedHour: Int, selectedMinute: Int ->
                selectedTimeText = "$selectedHour:$selectedMinute"
            }, hour, minute, true
        )
        timePicker.show()
    }
}