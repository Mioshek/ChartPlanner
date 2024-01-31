package com.mioshek.chartplanner.data.models.habits

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedDao{

    @Insert
    suspend fun insert(completed: Completed)

    @Query("DELETE FROM completed WHERE date = :date AND habitId = :hid ")
    suspend fun delete(date: String, hid: Int)

    @Query("SELECT * " +
            "FROM completed " +
            "WHERE habitId = :h_id")
    fun getByHabitId(h_id: Int): Flow<List<Completed>>

    @Query("SELECT * " +
            "FROM completed " +
            "WHERE date = :date")
    fun getByDate(date: String): Flow<List<Completed>>
}