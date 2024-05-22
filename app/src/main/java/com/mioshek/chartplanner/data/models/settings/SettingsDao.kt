package com.mioshek.chartplanner.data.models.settings

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao{

    @Query("INSERT INTO settings (settingName, value, displayType, visible) " +
            "SELECT :settingName, :value, :displayType, :visible " +
            "WHERE NOT EXISTS (SELECT 1 FROM SETTINGS WHERE settingName = :settingName);")
    suspend fun createIfNotPresent(settingName: Int, value: String, displayType: Int?, visible: Int)

    @Upsert
    suspend fun upsert(setting: Setting)

    @Query("DELETE FROM settings WHERE settingName = :settingName")
    suspend fun delete(settingName: Int)

    @Query("SELECT * " +
            "FROM settings " +
            "WHERE settingName = :settingName")
    fun getSetting(settingName: Int): Flow<Setting>

    @Query("SELECT * " +
            "FROM settings ")
    fun getAll(): Flow<List<Setting>>
}
