package com.mioshek.chartplanner.views

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mioshek.chartplanner.views.bars.BottomNavigationItem
import com.mioshek.chartplanner.views.charts.DrawGraph
import com.mioshek.chartplanner.views.habits.CalendarPicker
import com.mioshek.chartplanner.views.habits.EditHabit
import com.mioshek.chartplanner.views.habits.ListHabits
import com.mioshek.chartplanner.views.habits.NewHabit
import com.mioshek.chartplanner.views.settings.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController, startDestination = "main") {
        navigation(
            startDestination = BottomNavigationItem.Chart.route,
            route = "main"
        ){
            composable(BottomNavigationItem.Chart.route) {
                DrawGraph()
            }

            composable(BottomNavigationItem.Habits.route) {
                ListHabits(navController)
            }

            composable(BottomNavigationItem.Settings.route) {
                Settings()
            }
        }

        navigation(
            startDestination = "habits/new",
            route = "habits"
        ){
            composable(
                "habits/new"
            ){
                NewHabit(navController)
            }

            composable(
                "habits/{hid}",
                arguments = listOf(navArgument("hid") {type = NavType.IntType})
            ){ navBackStackEntry ->
                EditHabit(navBackStackEntry.arguments?.getInt("hid"))
            }

            composable(
                "habits/new/calendar"
            ){
                CalendarPicker(navController)
            }
        }
    }
}

private fun clearStack(navController: NavController){
    navController.navigate(navController.currentBackStack.toString()){
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
}


// Todo's
// Fix Animation speed between navigation elements