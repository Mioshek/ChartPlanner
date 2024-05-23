package com.mioshek.chartplanner

import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.mioshek.chartplanner.assets.presets.DefaultSettings
import com.mioshek.chartplanner.data.models.AppDatabase
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        if (Settings.Secure.getInt(this.contentResolver, "navigation_mode", 0) == 2){
            enableEdgeToEdge()
            WindowCompat.getInsetsController(window, window.decorView).run {
                hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        val context = this

        lifecycleScope.launch {
            DefaultSettings.loadPreset(context)
        }

        super.onCreate(savedInstanceState)

        setContent {
            ChartPlannerTheme {
                // A surface container using the 'surface' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
                    HabitMainView()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // close the primary database to ensure all the transactions are merged
        AppDatabase.closeDb()
    }
}



@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    ChartPlannerTheme {
        HabitMainView()
    }
}

// Todo's
// Font limit upper 30 lower 10