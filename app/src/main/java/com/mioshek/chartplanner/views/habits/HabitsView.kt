package com.mioshek.chartplanner.views.habits

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mioshek.chartplanner.R
import com.mioshek.chartplanner.data.models.habits.toHabitUiState
import com.mioshek.chartplanner.ui.AppViewModelProvider
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme
import java.text.SimpleDateFormat

@Composable
fun ListHabits(
    navController: NavController,
    modifier: Modifier = Modifier,
    habitsViewModel: ListHabitsViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val verticalScroll = rememberScrollState(0)
    val listHabitsUiState by habitsViewModel.habitUiState.collectAsState()

    Column(
        modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        habitsViewModel.updateListUi()
        val habitsList = listHabitsUiState.habits.collectAsState(listOf()).value
        if (habitsList.isNotEmpty()) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .verticalScroll(verticalScroll),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                habitsList.forEach {
                    HabitElement(it.toHabitUiState(), navController)
                }
            }
            NewHabitNavigation(navController, "New")
        }
        else{
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    text = "Habit List Empty",
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                )
                Spacer(modifier = modifier.padding(10.dp))
                NewHabitNavigation(navController, "New")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitElement(
    habit: HabitUiState,
    navController: NavController,
    modifier: Modifier = Modifier
){
    val dateFormat = SimpleDateFormat("dd MMM yyyy")
    Card(
        onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set("isInsertedToDb", false)
            navController.navigate("habits/${habit.id}")
        },          // add route
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.8f),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.2f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.2f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .padding(bottom = 10.dp)
    ) {
        val displayedNextOccurring = if (habit.date == null) "Starting Date" else dateFormat.format(
            habit.date
        )

        Column(
            modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(habit.name)
            Text(habit.description ?: "Description Empty")
            Text("Next: $displayedNextOccurring")
            Text("Interval: ${habit.intervalDays}")
        }
    }
}

@Composable
fun NewHabitNavigation(
    navController: NavController,
    buttonText: String,
    modifier:  Modifier = Modifier,
){
    Button(
        modifier = modifier,
        enabled = true,
        shape = ButtonDefaults.shape,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = null,
        border = null,
        contentPadding = PaddingValues(10.dp),
        onClick = {
            navController.navigate("habits/new")
        },
    ){
        Icon(
            painterResource(id = R.drawable.add),
            contentDescription = buttonText
        )
        Text("New")
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = "id:pixel_6_pro")
@Composable
fun BottomNavigationBarPreview() {
    ChartPlannerTheme {
//        ListHabits(rememberNavController())
    }
}