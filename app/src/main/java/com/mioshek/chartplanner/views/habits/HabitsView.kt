package com.mioshek.chartplanner.views.habits

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mioshek.chartplanner.R
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme
import com.mioshek.chartplanner.views.bars.BottomNavigationItem

@Composable
fun ListHabits(navController: NavController ,modifier: Modifier = Modifier){
    val habits = listOf<HabitUiState>(HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState())
    val verticalScroll = rememberScrollState(0)

    Column(
        modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Column (
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .verticalScroll(verticalScroll),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            habits.forEach{
                HabitElement(it)
            }
        }
        NewHabitNavigation(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitElement(
    habit: HabitUiState,
    modifier: Modifier = Modifier
){
    Card(
        onClick = { /* Do something */ },
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
        Column(
            modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(habit.name)
            Text(habit.description ?: "Description Empty")
            Text("Next: ${habit.nextOccurring}")
            Text("Interval: ${habit.intervalDays}")
        }
    }
}

@Composable
fun NewHabitNavigation(navController: NavController, modifier: Modifier = Modifier){
    Button(
        modifier = modifier,
        enabled = true,
        shape = ButtonDefaults.shape,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = null,
        border = null,
        contentPadding = PaddingValues(10.dp),
        onClick = {
            navController.navigate(BottomNavigationItem.Habits.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                navController.graph.startDestinationRoute?.let { route ->
                    popUpTo(route) {
                        saveState = true
                    }
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        },
    ){
        Icon(
            painterResource(id = R.drawable.add),
            contentDescription = "New"
        )
        Text("New")
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomNavigationBarPreview() {
    ChartPlannerTheme {
//        ListHabits()
    }
}