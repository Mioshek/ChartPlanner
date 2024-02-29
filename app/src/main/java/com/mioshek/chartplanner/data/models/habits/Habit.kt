package com.mioshek.chartplanner.data.models.habits

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mioshek.chartplanner.views.habits.HabitUiState

@Entity(tableName = "habits")
data class Habit(
    val name: String,
    val description: String,
    val date: Long,
    val intervalDays: Int,

    @PrimaryKey(autoGenerate = true)
    @NonNull
    val hid: Int = 0
)

fun Habit.toHabitUiState(): HabitUiState {
    return HabitUiState(hid, name, description, false, date, intervalDays)
}
