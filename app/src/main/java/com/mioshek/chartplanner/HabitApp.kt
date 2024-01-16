package com.mioshek.chartplanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mioshek.chartplanner.views.ChartPlannerNavigation
import com.mioshek.chartplanner.views.bars.BottomBar

@Composable
fun HabitApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier.fillMaxSize(),

//        topBar = {
//            appBars.TopAppBar()
//        },

        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                ChartPlannerNavigation(navController = navController)
            }
        },

        bottomBar = {
            BottomBar(navController)
        },

        containerColor = MaterialTheme.colorScheme.surface
    )
}