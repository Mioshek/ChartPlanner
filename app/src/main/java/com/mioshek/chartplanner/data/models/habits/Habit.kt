package com.mioshek.chartplanner.data.models.habits

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    val name: String,
    val description: String?,
    val startEpochDate: Int,
    val startEpochTime: Int,
    val endEpochDate: Int?,
    val endEpochTime: Int?,
    val intervalDays: Int,

    @PrimaryKey(autoGenerate = true)
    val hId: Int = 0
)

//fun Habit.toHabitUiState(): HabitUiState {
//    return HabitUiState(hid, name, description, false, date, intervalDays)
//}
