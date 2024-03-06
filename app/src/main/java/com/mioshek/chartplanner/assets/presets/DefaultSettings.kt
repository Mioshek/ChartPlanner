package com.mioshek.chartplanner.assets.presets

import com.mioshek.chartplanner.MainActivity
import com.mioshek.chartplanner.data.models.AppDatabase
import com.mioshek.chartplanner.data.models.settings.Setting
import java.util.TimeZone

class DefaultSettings {

    companion object{
        suspend fun loadPreset(context: MainActivity){
            val settingsDao = AppDatabase.getDatabase(context).settingsDao
            try {
                // Init Date
                settingsDao.createIfNotPresent(
                    "InitDate",
                    "${System.currentTimeMillis()/1000}",
                    0
                )

                //Init Font Size
                settingsDao.createIfNotPresent(
                    "InitFontSize",
                    "20",
                    1
                )

                settingsDao.createIfNotPresent(
                    "ShowCirclesAsGraphPoints",
                    "True",
                    1
                )

                settingsDao.createIfNotPresent(
                    "AllowCompletingHabitsAnytime",
                    "false",
                    1
                )
            } catch (e: Exception) {
                // Handle exceptions (e.g., SQLiteConstraintException if the record already exists)
                // Insert failed
                e.printStackTrace()
            }
        }
    }
}