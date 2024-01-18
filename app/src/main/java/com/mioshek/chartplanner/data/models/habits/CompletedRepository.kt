package com.mioshek.chartplanner.data.models.habits

import kotlinx.coroutines.flow.Flow

interface CompletedRepository {

    suspend fun insert(completed: Completed)

    suspend fun delete(completedId: Int)

    fun getByHabitId(h_id: Int): Flow<List<Completed>>

    fun getByDateRange(startDate: Long, endDate: Long): Flow<List<Completed>>
}

class OfflineCompletedRepository(private val completedDao: CompletedDao) : CompletedRepository {
    override suspend fun insert(completed: Completed) = completedDao.insert(completed)

    override suspend fun delete(completedId: Int) = completedDao.delete(completedId)

    override fun getByHabitId(h_id: Int): Flow<List<Completed>> = completedDao.getByHabitId(h_id)

    override fun getByDateRange(startDate: Long, endDate: Long): Flow<List<Completed>> = completedDao.getByDateRange(startDate, endDate)
}