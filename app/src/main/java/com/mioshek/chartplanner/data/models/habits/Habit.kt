package com.mioshek.chartplanner.data.models.habits

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    val name: String,
    val description: String,
    val firstDate: Long,
    val lastDate: Long,
    val intervalDays: Int,

    @PrimaryKey(autoGenerate = true)
    @NonNull
    val hid: Int = 0
)

//fun Habit.toHabitUiState(): HabitUiState {
//    return HabitUiState(hid, name, description, false, date, intervalDays)
//}
