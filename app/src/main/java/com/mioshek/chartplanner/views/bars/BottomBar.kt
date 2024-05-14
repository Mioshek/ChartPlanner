package com.mioshek.chartplanner.views.bars

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mioshek.chartplanner.R
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme


sealed class BottomNavigationItem(var route: String, var icon: Int) {
    object Chart : BottomNavigationItem("chart", R.drawable.chart_line)
    object Habits : BottomNavigationItem("habit", R.drawable.calendar_check)

    object Settings: BottomNavigationItem( "settings", R.drawable.settings)
}


@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavigationItem.Chart,
        BottomNavigationItem.Habits,
        BottomNavigationItem.Settings
    )

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 5.dp,
    ){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEachIndexed{ index, item ->
            var title = ""
            when(item.route){
                "chart" -> {title = stringResource(R.string.chart)}
                "habit" -> {title = stringResource(R.string.habits)}
                "settings" -> {title = stringResource(R.string.settings)}
            }

            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = title,
                    )
                },
                label = { Text(text = title) },
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.tertiary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.surface,
                    unselectedIconColor = MaterialTheme.colorScheme.tertiary.copy(0.4f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledIconColor = MaterialTheme.colorScheme.tertiary.copy(0.1f),
                    disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(0.1f)
                ),
                onClick = {
                    navController.navigate(item.route) {
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
            )
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomNavigationBarPreview() {
    ChartPlannerTheme {
        val nav = rememberNavController()
        BottomBar(nav)
    }
}