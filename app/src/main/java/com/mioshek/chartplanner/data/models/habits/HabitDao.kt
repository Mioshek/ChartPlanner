package com.mioshek.chartplanner.data.models.habits

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao{

    @Insert
    suspend fun insertHabit(habit: Habit)

    @Query("UPDATE habits" +
            " SET name=:name, description=:description, completed=:completed, date=:date, intervalDays=:intervalDays" +
            " WHERE hid == :h_id")
    suspend fun updateHabit(
        h_id: Int,
        name: String,
        description: String,
        completed: Boolean,
        date: Long,
        intervalDays: Int,
    )

    @Query("DELETE FROM habits WHERE hid =:h_id")
    suspend fun delete(h_id: Int)

    @Query("SELECT * from habits WHERE hid = :h_id")
    fun getHabit(h_id: Int): Flow<Habit>

    @Query("SELECT * from habits ORDER BY name ASC")
    fun getAllHabits(): Flow<List<Habit>>
}