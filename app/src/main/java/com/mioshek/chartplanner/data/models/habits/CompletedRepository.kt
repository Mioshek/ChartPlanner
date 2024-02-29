package com.mioshek.chartplanner.data.models.habits

import kotlinx.coroutines.flow.Flow

interface CompletedRepository {
    suspend fun insert(completed: Completed)
    suspend fun delete(date: Long, hid: Int)
    fun getByDate(date: Long): Flow<List<Completed>>
    suspend fun getChartStatistics(startingDay: Int, numberOfDays: Int): List<Float>
}

class OfflineCompletedRepository(private val completedDao: CompletedDao) : CompletedRepository {
    override suspend fun insert(completed: Completed) = completedDao.insert(completed)

    override suspend fun delete(date: Long, hid: Int) = completedDao.delete(date, hid)

    override fun getByDate(date: Long): Flow<List<Completed>> = completedDao.getByDate(date)
    override suspend fun getChartStatistics(startingDay: Int, numberOfDays: Int): List<Float> = completedDao.getCompletedPercentage(startingDay, numberOfDays)
}