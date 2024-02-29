package com.mioshek.chartplanner.views.habits

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ListHabits(
    navController: NavController,
    modifier: Modifier = Modifier,
    habitsViewModel: ListHabitsViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val verticalScroll = rememberScrollState(0)
    var choosingDate by remember { mutableStateOf(true)}
    val listHabitsUiState by habitsViewModel.habitUiState.collectAsState()
    var displayedDate by remember { mutableLongStateOf(System.currentTimeMillis() / 1000) }
    val chosenDate = navController.currentBackStackEntry?.savedStateHandle?.get<Long?>("date")
    if (chosenDate != null && choosingDate){
        displayedDate = chosenDate
        choosingDate = false
    }
    var changeDate by remember { mutableStateOf(false) }

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
                                    if (displayedDate > 1704150000){ // 01.01.2024 12:00 AM
                                        displayedDate -= (60 * 60 * 24)
                                        changeDate = false
                                    }
                                }

                                x < 0 -> {
                                    displayedDate += (60 * 60 * 24)
                                    changeDate = false
                                }
                            }
                        }
                    },
                    onDragEnd = {
                        changeDate = true

                    }
                )
            }
    ){
        habitsViewModel.UpdateListUi(displayedDate)
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
                    modifier = Modifier.padding(5.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .weight(2f)
                            .clickable {
                                displayedDate -= (60 * 60 * 24)
                            }) {
                        Icon(Icons.Default.KeyboardArrowLeft, "Left")
                    }

                    Text(
                        "Date: ${DateFormatter.sdf.format(displayedDate * 1000).substring(0, 10)}",
                        modifier = Modifier
                            .weight(3f)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                choosingDate = false
                                navController.navigate("habits/calendar")
                            }
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onBackground.copy(0.5f),
                                shape = RoundedCornerShape(8.dp) // Adjust the corner radius as needed
                            )
                            .padding(start = 6.dp, bottom = 2.dp, end = 0.dp, top = 2.dp)
                    )

                    Box(contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .weight(2f)
                            .clickable {
                                displayedDate += (60 * 60 * 24)
                            }
                    ) {
                        Icon(Icons.Default.KeyboardArrowRight, "Right")
                    }
                }
            }

            Spacer(modifier.padding(10.dp))

            habitsList.forEach { value ->
                HabitElement(value, navController, habitsViewModel, displayedDate)
            }
        }
        NewHabitNavigation(navController, "New")
    }
}

@Composable
fun HabitElement(
    habit: HabitUiState,
    navController: NavController,
    habitsViewModel: ListHabitsViewModel,
    date: Long,
    modifier: Modifier = Modifier
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.2f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.2f)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .padding(bottom = 10.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { habitsViewModel.changeSelection(true) },
                    onTap = {
                        if (habitsViewModel.habitUiState.value.enterSelectMode) {
                            habitsViewModel.changeSelection(false)
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
                                    habitsViewModel.tickUndoneHabit(habit.id!!, date)
                                } else {
                                    habitsViewModel.completeHabit(habit.id!!, date)
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = "id:pixel_6_pro")
@Composable
fun BottomNavigationBarPreview() {
    ChartPlannerTheme {
//        NewHabitNavigation()
    }
}