package com.mioshek.chartplanner

import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme
import com.mioshek.chartplanner.views.Navigation
import com.mioshek.chartplanner.views.bars.BottomNavigationBar
import java.util.prefs.Preferences

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Settings.Secure.getInt(this.contentResolver, "navigation_mode", 0) == 2){
            enableEdgeToEdge()
            WindowCompat.getInsetsController(window, window.decorView)?.run {
                hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        super.onCreate(savedInstanceState)

        setContent {
            ChartPlannerTheme {
                // A surface container using the 'surface' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
                    Main()
                }
            }
        }
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier.fillMaxSize(),

//        topBar = {
//            appBars.TopAppBar()
//        },

        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                Navigation(navController = navController)
            }
        },

        bottomBar = {
            BottomNavigationBar(navController)
        },

        containerColor = MaterialTheme.colorScheme.surface
    )
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    ChartPlannerTheme {
        Main()
    }
}

// Todo's
// Font limit upper 30 lower 10