package com.mioshek.chartplanner.data.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mioshek.chartplanner.data.models.habits.Completed
import com.mioshek.chartplanner.data.models.habits.CompletedDao
import com.mioshek.chartplanner.data.models.habits.Habit
import com.mioshek.chartplanner.data.models.habits.HabitDao

@Database(entities = [Habit::class, Completed::class], version = 1)

abstract class AppDatabase : RoomDatabase() {

    abstract val habitDao: HabitDao
    abstract val completedDao: CompletedDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "ChartPlanner")
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        fun closeDb(){
            INSTANCE?.close()
        }
    }
}