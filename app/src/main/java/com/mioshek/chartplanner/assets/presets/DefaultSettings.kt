package com.mioshek.chartplanner.assets.presets

import android.util.Log
import com.mioshek.chartplanner.MainActivity
import com.mioshek.chartplanner.R
import com.mioshek.chartplanner.data.models.AppDatabase

class DefaultSettings {

    companion object{
        suspend fun loadPreset(context: MainActivity){
            val settingsDao = AppDatabase.getDatabase(context).settingsDao
            try {
                // Init Language
                settingsDao.createIfNotPresent(
                    R.string.language,
                    "EN",
                    3,
                    1
                )

                // Init Date
                settingsDao.createIfNotPresent(
                    R.string.date,
                    "${System.currentTimeMillis()/1000}",
                    0,
                    0
                )

                //Init Font Size
                settingsDao.createIfNotPresent(
                    R.string.font_size,
                    "20",
                    1,
                    1
                )

                settingsDao.createIfNotPresent(
                    R.string.show_circles_on_graph,
                    "True",
                    2,
                    1
                )

                settingsDao.createIfNotPresent(
                    R.string.allow_completing_habits_anytime,
                    "false",
                    2,
                    1
                )

            } catch (e: Exception) {
                // Handle exceptions (e.g., SQLiteConstraintException if the record already exists)
                // Insert failed
                Log.d("Load Preset Error", e.toString())
            }
        }
    }
}