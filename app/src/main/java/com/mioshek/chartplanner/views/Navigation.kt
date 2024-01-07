package com.mioshek.chartplanner.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mioshek.chartplanner.views.bars.NavigationItem
import com.mioshek.chartplanner.views.graphs.ShowGraphBackground
import com.mioshek.chartplanner.views.habits.CreateNewHabit

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Chart.route) {
        composable(NavigationItem.Chart.route) {
            ShowGraphBackground()
        }
        composable(NavigationItem.Habits.route) {
            CreateNewHabit()
        }
    }
}