package com.mioshek.chartplanner.assets.presets

import android.util.Log
import com.mioshek.chartplanner.MainActivity
import com.mioshek.chartplanner.data.models.AppDatabase

class DefaultSettings {

    companion object{
        suspend fun loadPreset(context: MainActivity){
            val settingsDao = AppDatabase.getDatabase(context).settingsDao
            try {
                // Init Language
                settingsDao.createIfNotPresent(
                    "InitLanguage",
                    "EN",
                    3,
                    1
                )

                // Init Date
                settingsDao.createIfNotPresent(
                    "InitDate",
                    "${System.currentTimeMillis()/1000}",
                    0,
                    0
                )

                //Init Font Size
                settingsDao.createIfNotPresent(
                    "InitFontSize",
                    "20",
                    1,
                    1
                )

                settingsDao.createIfNotPresent(
                    "InitShowCirclesAsGraphPoints",
                    "True",
                    2,
                    1
                )

                settingsDao.createIfNotPresent(
                    "InitAllowCompletingHabitsAnytime",
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