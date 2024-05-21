package com.mioshek.chartplanner.data.models.habits

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedDao{

    @Insert
    suspend fun insert(completed: Completed)

    @Query("DELETE FROM completed WHERE date = :date AND hId = :hId AND time = :time")
    suspend fun delete(hId: Int, date: Int, time: Int)

    @Query("SELECT * " +
            "FROM completed " +
            "WHERE hId = :hId"
    )
    fun getByHabitId(hId: Int): Flow<List<Completed>>

    @Query("SELECT * " +
            "FROM completed " +
            "WHERE date = :date")
    fun getByDate(date: Int): Flow<List<Completed>>

    @Query("SELECT *" +
            "FROM completed")
    fun getAllCompleted(): Flow<List<Completed>>

    @Query("WITH RECURSIVE TimestampFilter AS (" +
            "    SELECT :startingDay AS target_timestamp" +
            "), " +
            "DateSeries AS (" +
            "    SELECT 0 AS n" +
            "    UNION ALL" +
            "    SELECT n + 1 FROM DateSeries, TimestampFilter WHERE n < :numberOfDays - 1" +
            "), " +
            "H AS (" +
            "    SELECT *" +
            "    FROM habits" +
            "), " +
            "Query1 AS (" +
            "    SELECT" +
            "        :startingDay + n AS displayedDate," +
            "        COUNT(" +
            "            CASE" +
            "                WHEN target_timestamp + n - H.startEpochDate >= 0" +
            "                     AND ((target_timestamp + n - h.startEpochDate) % h.intervalDays = 0" +
            "                     OR (intervalDays = 0 AND target_timestamp + n - h.startEpochDate = 0))" +
            "                THEN 1" +
            "                ELSE NULL" +
            "            END" +
            "        ) AS HowMany" +
            "    FROM DateSeries" +
            "    JOIN TimestampFilter" +
            "    JOIN H" +
            "    GROUP BY displayedDate" +
            "), " +
            "Query2 AS (" +
            "    SELECT" +
            "        completed.date AS displayedDate," +
            "        CAST(COUNT(*) AS REAL) AS HowMany" +
            "    FROM completed" +
            "    WHERE completed.date >= :startingDay AND completed.date <= :startingDay + :numberOfDays" +
            "    GROUP BY completed.date" +
            ") " +
            "SELECT COALESCE(Query2.HowMany * 100/ Query1.HowMany, 0) " +
            "FROM Query1 " +
            "LEFT JOIN Query2 ON Query1.displayedDate = Query2.displayedDate ")
    suspend fun getCompletedPercentage(startingDay: Int, numberOfDays: Int): List<Float>
}