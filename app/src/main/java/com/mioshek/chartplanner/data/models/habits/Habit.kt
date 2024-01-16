package com.mioshek.chartplanner.data.models.habits

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mioshek.chartplanner.views.habits.HabitUiState
import java.util.Date

@Entity(tableName = "habits")
data class Habit(
    val name: String,
    val description: String,
    val completed: Boolean,
    val date: Long,
    val intervalDays: Int,

    @PrimaryKey(autoGenerate = true)
    val hid: Int = 0
)

fun Habit.toHabitUiState(): HabitUiState {
    return HabitUiState(hid, name, description, completed, date, intervalDays)
}
