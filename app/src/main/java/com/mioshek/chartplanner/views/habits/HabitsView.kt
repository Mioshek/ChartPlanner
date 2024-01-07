package com.mioshek.chartplanner.views.habits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListHabits(modifier: Modifier = Modifier){
    val habits = listOf<HabitUiState>(HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState(), HabitUiState())
    val verticalScroll = rememberScrollState(0)

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 20.dp, end = 20.dp)
            .verticalScroll(verticalScroll),
        horizontalAlignment = Alignment.CenterHorizontally,
        ){
        habits.forEach{
            HabitElement(it)
        }
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