package com.mioshek.chartplanner.data.models.habits

import kotlinx.coroutines.flow.Flow

interface CompletedRepository {

    suspend fun insert(completed: Completed)

    suspend fun delete(date: String, hid: Int)


    fun getByDate(date: String): Flow<List<Completed>>
}

class OfflineCompletedRepository(private val completedDao: CompletedDao) : CompletedRepository {
    override suspend fun insert(completed: Completed) = completedDao.insert(completed)

    override suspend fun delete(date: String, hid: Int) = completedDao.delete(date, hid)

    override fun getByDate(date: String): Flow<List<Completed>> = completedDao.getByDate(date)
}