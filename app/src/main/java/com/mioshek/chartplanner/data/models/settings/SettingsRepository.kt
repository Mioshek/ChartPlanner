package com.mioshek.chartplanner.data.models.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun upsert(setting: Setting)
    suspend fun delete(settingName: Int)
    suspend fun createIfNotPresent(settingName: Int, value: String, displayType: Int?, visible: Int)
    fun getSetting(settingName: Int): Flow<Setting>
    fun getAll(): Flow<List<Setting>>
}

class OfflineSettingsRepository(private val settingsDao: SettingsDao) : SettingsRepository {
    override fun getSetting(settingName: Int) = settingsDao.getSetting(settingName)
    override fun getAll(): Flow<List<Setting>> = settingsDao.getAll()
    override suspend fun upsert(setting: Setting) = settingsDao.upsert(setting)
    override suspend fun delete(settingName: Int) = settingsDao.delete(settingName)
    override suspend fun createIfNotPresent(settingName: Int, value: String, displayType: Int?, visible: Int) = settingsDao.createIfNotPresent(settingName, value, displayType, visible)
}