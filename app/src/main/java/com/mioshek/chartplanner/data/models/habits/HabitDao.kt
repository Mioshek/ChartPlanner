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
        date: Long,
        intervalDays: Int,
    )

    @Query("DELETE FROM habits WHERE hid =:h_id")
    suspend fun delete(h_id: Int)

    @Query("SELECT * FROM habits WHERE hid = :h_id")
    fun get(h_id: Int): Flow<Habit>

    @Query("SELECT * FROM habits ORDER BY name ASC")
    fun getAll(): Flow<List<Habit>>

//    @Query("SELECT *" +
//            "FROM HABITS " +
//            "WHERE (JULIANDAY(substr(:date, 7, 4) || '-' || substr(:date, 4, 2) || '-' " +
//            "|| substr(:date, 1, 2)) - julianday(substr(date, 7, 4) || '-' || substr(date, 4, 2) " +
//            "|| '-' || substr(date, 1, 2))) % intervalDays = 0\n or (intervalDays == 0 and date = :date)" +
//            "ORDER BY name ASC") 1 day -> 86400s When current time in milis devided by 1k and 86400 then I get days since beginning of 1970
    @Query("SELECT *" +
            "FROM habits " +
            "WHERE ((:currentDate  / 86400 - date / 86400) % intervalDays = 0 AND date <= :currentDate) OR (intervalDays == 0 AND :currentDate  / 86400 = date / 86400)")
    fun getAllByDate(currentDate: Long): Flow<List<Habit>>
}