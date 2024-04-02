package com.mioshek.chartplanner.data.models.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun upsert(setting: Setting)
    suspend fun delete(settingName: String)
    suspend fun createIfNotPresent(settingName: String, value: String, displayType: Int?, visible: Int)
    fun getSetting(settingName: String): Flow<Setting>
    fun getAll(): Flow<List<Setting>>
}

class OfflineSettingsRepository(private val settingsDao: SettingsDao) : SettingsRepository {
    override fun getSetting(settingName: String) = settingsDao.getSetting(settingName)
    override fun getAll(): Flow<List<Setting>> = settingsDao.getAll()
    override suspend fun upsert(setting: Setting) = settingsDao.upsert(setting)
    override suspend fun delete(settingName: String) = settingsDao.delete(settingName)
    override suspend fun createIfNotPresent(settingName: String, value: String, displayType: Int?, visible: Int) = settingsDao.createIfNotPresent(settingName, value, displayType, visible)
}