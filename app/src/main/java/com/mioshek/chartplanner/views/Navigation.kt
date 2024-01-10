package com.mioshek.chartplanner.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mioshek.chartplanner.R
import com.mioshek.chartplanner.views.bars.BottomNavigationItem
import com.mioshek.chartplanner.views.charts.DrawGraph
import com.mioshek.chartplanner.views.habits.HabitsNavigationItem
import com.mioshek.chartplanner.views.habits.ListHabits
import com.mioshek.chartplanner.views.habits.NewHabit
import com.mioshek.chartplanner.views.settings.Settings

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavigationItem.Chart.route) {
        composable(BottomNavigationItem.Chart.route) {
            DrawGraph()
        }
        composable(BottomNavigationItem.Habits.route) {
            ListHabits(navController)
        }

        composable(BottomNavigationItem.Settings.route) {
            Settings()
        }
        
        composable(HabitsNavigationItem.NewHabit.route){
            NewHabit(navController)
        }
        
    }
}


// Todo's
// Fix Animation speed between navigation elements