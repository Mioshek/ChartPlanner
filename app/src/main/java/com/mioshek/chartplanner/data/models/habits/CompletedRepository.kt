package com.mioshek.chartplanner.data.models.habits

import kotlinx.coroutines.flow.Flow

interface CompletedRepository {
    suspend fun insert(completed: Completed)
    suspend fun delete(hid: Int, date: Int, time: Int)
    suspend fun getAllCompleted(): Flow<List<Completed>>
    fun getByDate(date: Int): Flow<List<Completed>>
    suspend fun getChartStatistics(startingDay: Int, numberOfDays: Int): List<Float>
}

class OfflineCompletedRepository(private val completedDao: CompletedDao) : CompletedRepository {
    override suspend fun insert(completed: Completed) = completedDao.insert(completed)

    override suspend fun delete(hid: Int, date: Int, time: Int) = completedDao.delete(hid, date, time)
    override suspend fun getAllCompleted(): Flow<List<Completed>> = completedDao.getAllCompleted()
    override fun getByDate(date: Int): Flow<List<Completed>> = completedDao.getByDate(date)
    override suspend fun getChartStatistics(startingDay: Int, numberOfDays: Int): List<Float> = completedDao.getCompletedPercentage(startingDay, numberOfDays)
}