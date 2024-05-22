package com.mioshek.chartplanner.views.habits

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mioshek.chartplanner.R
import com.mioshek.chartplanner.assets.formats.DateFormatter
import com.mioshek.chartplanner.ui.AppViewModelProvider
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListHabits(
    navController: NavController,
    modifier: Modifier = Modifier,
    habitsViewModel: ListHabitsViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val verticalScroll = rememberScrollState(0)
    var choosingDate by remember { mutableStateOf(true)}
    val listHabitsUiState by habitsViewModel.habitUiState.collectAsState()
    var chosenDay by remember { mutableIntStateOf(((System.currentTimeMillis() + DateFormatter.timezoneOffset) / 86400000).toInt())}
    val chosenDate = navController.currentBackStackEntry?.savedStateHandle?.get<Long?>("pickedDate")
    var changeDate by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    if (chosenDate != null && choosingDate){
        chosenDay = ((chosenDate + DateFormatter.timezoneOffset/1000)/ 86400).toInt()
        choosingDate = false
    }

    Box(
        modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        val (x, _) = dragAmount
                        if (changeDate) {
                            when {
                                x > 0 -> {
                                    if (chosenDay > 19723) { // 01.01.2024 00:00 AM UTC
                                        chosenDay -= 1
                                        changeDate = false
                                    }
                                }

                                x < 0 -> {
                                    chosenDay += 1
                                    changeDate = false
                                }
                            }
                        }
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            delay(200)
                            changeDate = true
                        }
                    }
                )
            }
    ){
        habitsViewModel.UpdateListUi(chosenDay) // UTC from database
        val habitsList = listHabitsUiState.habits

        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(verticalScroll),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                if (chosenDay > 19723) { // 01.01.2024 00:00 AM UTC
                                    chosenDay -= 1
                                    coroutineScope.launch {
                                        changeDate = false
                                        delay(200)
                                        changeDate = true
                                    }
                                }
                            }) {
                        Icon(Icons.Default.KeyboardArrowLeft, "Left")
                    }

                    AnimatedVisibility(
                        visible = changeDate,
                        enter = scaleIn(animationSpec = keyframes {
                            this.durationMillis = 150
                        },
                            0.8f
                        ),
                        exit = scaleOut(animationSpec = keyframes {
                            this.durationMillis = 150
                        },
                            0.8f),
                    ) {
                        Text(
                            stringResource(R.string.date) +": ${DateFormatter.sdf.format(chosenDay.toLong() * 86400000).substring(0,10)}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(3f)
                                .clickable {
                                    choosingDate = false
                                    navController.navigate("habits/calendar")
                                }
                                .padding(2.dp)
                        )
                    }

                    Box(contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                chosenDay += 1
                                coroutineScope.launch {
                                    changeDate = false
                                    delay(200)
                                    changeDate = true
                                }
                            }
                    ) {
                        Icon(Icons.Default.KeyboardArrowRight, "Right")
                    }
                }
            }

            Spacer(modifier.padding(10.dp))

            habitsList.forEach { value ->
                AnimatedVisibility(
                    visible = changeDate,
                    enter = scaleIn(animationSpec = keyframes {
                        this.durationMillis = 150
                    },
                        0.5f
                    ),

                    exit = scaleOut(animationSpec = keyframes {
                        this.durationMillis = 150
                    }
                    ,
                        0.5f
                    ),
                ) {
                    HabitElement(value, navController, habitsViewModel, chosenDay)
                }
            }
        }
        NewHabitNavigation(navController, stringResource(R.string.newValue))
        if (listHabitsUiState.enterSelectMode){
            DeleteSelectedPopup(habitsViewModel)
        }
    }
}

@Composable
fun HabitElement(
    habit: HabitUiState,
    navController: NavController,
    habitsViewModel: ListHabitsViewModel,
    date: Int,
    modifier: Modifier = Modifier
){
    var selection by remember{
        mutableStateOf(BorderStroke(0.dp, Color.Transparent))
    }
    if (!habitsViewModel.habitUiState.collectAsState().value.enterSelectMode){
        selection = BorderStroke(0.dp, Color.Transparent)
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.2f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.2f)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        border = selection,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .padding(bottom = 10.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        if (habitsViewModel.habitUiState.value.enterSelectMode) {
                            habitsViewModel.changeSelection(false)
                        } else {
                            habitsViewModel.changeSelection(true)
                        }
                    },
                    onTap = {
                        if (habitsViewModel.habitUiState.value.enterSelectMode) {
                            if (habitsViewModel.isHabitSelected(habit)) {
                                habitsViewModel.unselectHabit(habit)
                                selection = BorderStroke(0.dp, Color.Transparent)
                            } else {
                                habitsViewModel.selectHabit(habit)
                                selection = BorderStroke(2.dp, Color.Blue)
                            }
                        } else {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "isInsertedToDb",
                                false
                            )
                            navController.navigate("habits/${habit.id}")
                        }
                    }
                )
            },
    ) {
        val coroutineScope = rememberCoroutineScope()

        Box(
            modifier
                .fillMaxSize()
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // The Text elements
                Text(habit.name)

                // The Icon element aligned to the right
                Icon(
                    painter = if (habit.done) painterResource(R.drawable.check_circle) else painterResource(R.drawable.radio_button_unchecked),
                    contentDescription = "Completed",
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                if (habit.done) {
                                    habitsViewModel.changeTickState(
                                        habit.id!!,
                                        date,
                                        habit.startEpochTime!!,
                                        true
                                    )
                                } else {
                                    habitsViewModel.changeTickState(
                                        habit.id!!,
                                        date,
                                        habit.startEpochTime!!,
                                        false
                                    )
                                }
                            }
                        }
//                        .weight(0.1f) // Adjust the weight as needed
                        .wrapContentWidth(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun NewHabitNavigation(
    navController: NavController,
    buttonText: String,
    modifier:  Modifier = Modifier,
){
    var glowSize by remember{ mutableStateOf(0.dp) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .offset(y = (-10).dp, x = (-5).dp)
    ){
        LaunchedEffect(true) {
            var countUp = true
            while (true) {
                if (countUp){
                    glowSize += 1.dp
                    delay(50)
                }
                else{
                    glowSize -= 1.dp
                    delay(50)
                }

                when(glowSize){
                    0.dp ->{
                        countUp = true
                    }
                    15.dp ->{
                        countUp = false
                    }
                }

            }
        }

        Box(modifier = modifier
            .offset(glowSize, glowSize)
            .align(Alignment.BottomEnd)
        ){
            Box(
                modifier = modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(0.4f),
                        shape = CircleShape
                    )
                    .offset(-glowSize, -glowSize)
                    .padding(8.dp + glowSize)
                    .align(Alignment.BottomEnd)

            ){
                Row{
                    Icon(
                        painterResource(id = R.drawable.add),
                        contentDescription = "Add",
                        tint = Color.Transparent
                    )
                    Text(buttonText, color = Color.Transparent)
                }
            }
        }

        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                .padding(8.dp)
                .align(Alignment.BottomEnd)
                .clickable {
                    navController.navigate("habits/new")
                }
        ){
            Row{
                Icon(
                    painterResource(id = R.drawable.add),
                    contentDescription = "Add"
                )
                Text(buttonText)
            }
        }
    }
}

@Composable
fun DeleteSelectedPopup(
    habitsViewModel: ListHabitsViewModel,
    modifier: Modifier = Modifier
){
    val coroutineScope = rememberCoroutineScope()
    var displayAlert by remember { mutableStateOf(false) }
    Box(
        modifier.fillMaxSize()
    ){
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                .padding(8.dp)
                .align(Alignment.BottomStart)
                .clickable {
                    displayAlert = true
                }
        ){
            Row{
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Add"
                )
                Text(stringResource(R.string.delete))
            }
        }
        if (displayAlert){
            AlertDialog(
                icon = {
                    Icon(Icons.Filled.Warning, contentDescription = "Example Icon")
                },
                title = {
                    Text(text = stringResource(R.string.delete))
                },
                text = {
                    Text(text = "Do You Want To Delete All Selected Habits?")
                },
                onDismissRequest = { displayAlert = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch{
                                habitsViewModel.removeSelectedHabits()
                                displayAlert = false
                                habitsViewModel.changeSelection(false)
                            }
                        }
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { displayAlert = false}
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = "id:pixel_6_pro")
@Composable
fun BottomNavigationBarPreview() {
    ChartPlannerTheme {
//        NewHabitNavigation()
    }
}