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
            " SET name=:name, description=:description, firstDate=:firstDate, lastDate=:lastDate, intervalDays=:intervalDays" +
            " WHERE hid == :h_id")
    suspend fun update(
        h_id: Int,
        name: String,
        description: String,
        firstDate: Long,
        lastDate: Long,
        intervalDays: kotlin.Short,
    )

    @Query("DELETE FROM habits WHERE hid =:h_id")
    suspend fun delete(h_id: Int)

    @Query("SELECT * FROM habits WHERE hid = :h_id")
    fun get(h_id: Int): Flow<Habit>

    @Query("SELECT * FROM habits ORDER BY name ASC")
    fun getAll(): Flow<List<Habit>>

    @Query("SELECT * " +
            "FROM habits " +
            "WHERE ((:currentDate / 86400 - firstDate / 86400) % intervalDays = 0 AND firstDate <= :currentDate " +
            "AND lastDate >= :currentDate) OR (intervalDays == 0 AND :currentDate  / 86400 = firstDate / 86400)")
    fun getAllByDate(currentDate: Long): Flow<List<Habit>>
}