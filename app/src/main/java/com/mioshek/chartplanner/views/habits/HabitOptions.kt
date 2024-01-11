package com.mioshek.chartplanner.views.habits

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EditHabit(id: Int? ,modifier: Modifier = Modifier){
    Box(modifier = modifier.fillMaxSize()){
        Text("new Habit {id}".format(id))
    }
}