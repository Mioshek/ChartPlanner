package com.mioshek.chartplanner.views.habits

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mioshek.chartplanner.views.bars.BottomNavigationItem

sealed class HabitsNavigationItem(var route: String, var title: String) {
    object NewHabit: HabitsNavigationItem( "new_habit", "New Habit")
}

@Composable
fun NewHabit(navController: NavController, modifier: Modifier = Modifier){
    Box(
        modifier = modifier.fillMaxSize()
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
//            TextField()

        }

        Row(

        ){
            Button(
                onClick = {}
            ){
                Text("Save")
            }

            Button(
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
                Text("Cancel")
            }
        }
    }
}