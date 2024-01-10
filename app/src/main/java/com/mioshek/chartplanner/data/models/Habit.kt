package com.mioshek.chartplanner.data.models

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import java.util.Calendar

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "habit_id") val hid: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "date") val date: Calendar,
    @ColumnInfo(name = "interval_days") val intervalDays: Int
)

@Dao
interface HabitDao{
    @Query("SELECT * FROM habit")
    suspend fun getAll(): List<Habit>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(vararg habits: Habit)

    @Delete
    suspend fun deleteHabits(vararg habits: Habit)

    @Update
    suspend fun updateHabits(songs: List<Habit>): Int
}