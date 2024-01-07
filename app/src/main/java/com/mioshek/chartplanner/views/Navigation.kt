package com.mioshek.chartplanner.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mioshek.chartplanner.views.bars.NavigationItem
import com.mioshek.chartplanner.views.graphs.DrawGraph
import com.mioshek.chartplanner.views.habits.ListHabits
import com.mioshek.chartplanner.views.settings.Settings

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Chart.route) {
        composable(NavigationItem.Chart.route) {
            DrawGraph()
        }
        composable(NavigationItem.Habits.route) {
            ListHabits()
        }

        composable(NavigationItem.Settings.route) {
            Settings()
        }
    }
}