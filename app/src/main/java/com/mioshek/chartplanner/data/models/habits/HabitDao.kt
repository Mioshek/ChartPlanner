package com.mioshek.chartplanner.data.models.habits

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao{

    @Insert
    suspend fun insert(habit: Habit)

    @Query("UPDATE habits " +
            "SET name=:name, description=:description, startEpochDate=:startEpochDate, startEpochTime=:startEpochTime, endEpochDate=:endEpochDate, endEpochTime=:endEpochTime, intervalDays=:intervalDays " +
            "WHERE hId == :hId"
    )
    suspend fun update(
        hId: Int,
        name: String,
        description: String?,
        startEpochDate: Int,
        startEpochTime: Int,
        endEpochDate: Int?,
        endEpochTime: Int?,
        intervalDays: Int,
    )

    @Query("DELETE FROM habits WHERE hId =:hId")
    suspend fun delete(hId: Int)

    @Query("SELECT * FROM habits WHERE hId = :hId")
    fun get(hId: Int): Flow<Habit>

    @Query("SELECT * FROM habits ORDER BY name ASC")
    fun getAll(): Flow<List<Habit>>

    @Query("SELECT * " +
            "FROM habits " +
            "WHERE (" +
            "(:date - startEpochDate) % intervalDays = 0 " +
            "AND :date >= startEpochDate " +
            "AND (CASE WHEN endEpochDate IS NOT NULL THEN :date <= endEpochDate ELSE 1 END) " +
            "    ) " +
            "OR (intervalDays = 0 AND startEpochDate = :date)")
    fun getAllByDate(date: Int): Flow<List<Habit>>
}