package com.mioshek.chartplanner.data.models.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun insert(setting: Setting)
    suspend fun delete(settingName: String)
    fun getSetting(settingName: String): Setting
    fun getAll(): Flow<List<Setting>>
    suspend fun update(settingName: String, value: String)
}

class OfflineSettingsRepository(private val settingsDao: SettingsDao) : SettingsRepository {
    override fun getSetting(settingName: String) = settingsDao.getSetting(settingName)
    override fun getAll(): Flow<List<Setting>> = settingsDao.getAll()
    override suspend fun insert(setting: Setting) = settingsDao.insert(setting)
    override suspend fun delete(settingName: String) = settingsDao.delete(settingName)
    override suspend fun update(settingName: String, value: String) = settingsDao.update(settingName, value)
}