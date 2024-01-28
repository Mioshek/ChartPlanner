package com.mioshek.chartplanner.data.models.habits

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao{

    @Insert
    suspend fun insert(habit: Habit)

    @Query("UPDATE habits" +
            " SET name=:name, description=:description, date=:date, intervalDays=:intervalDays" +
            " WHERE hid == :h_id")
    suspend fun update(
        h_id: Int,
        name: String,
        description: String,
        date: String,
        intervalDays: Int,
    )

    @Query("DELETE FROM habits WHERE hid =:h_id")
    suspend fun delete(h_id: Int)

    @Query("SELECT * FROM habits WHERE hid = :h_id")
    fun get(h_id: Int): Flow<Habit>

    @Query("SELECT * FROM habits ORDER BY name ASC")
    fun getAll(): Flow<List<Habit>>

    @Query("SELECT *" +
            "FROM HABITS " +
            "WHERE (JULIANDAY(substr(:date, 7, 4) || '-' || substr(:date, 4, 2) || '-' || substr(:date, 1, 2)) - julianday(substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2))) % intervalDays = 0\n " +
            "ORDER BY name ASC")
    fun getAllByDate(date: String): Flow<List<Habit>>
}