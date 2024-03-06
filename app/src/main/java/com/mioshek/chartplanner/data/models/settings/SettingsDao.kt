package com.mioshek.chartplanner.data.models.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao{

    @Query("INSERT INTO settings (settingName, value, visible) " +
            "SELECT :settingName, :value, :visible " +
            "WHERE NOT EXISTS (SELECT 1 FROM SETTINGS WHERE settingName = :settingName);")
    suspend fun createIfNotPresent(settingName: String, value: String, visible: Int)

    @Upsert
    suspend fun upsert(setting: Setting)

    @Query("DELETE FROM settings WHERE settingName = :settingName")
    suspend fun delete(settingName: String)

    @Query("SELECT * " +
            "FROM settings " +
            "WHERE settingName = :settingName")
    fun getSetting(settingName: String): Flow<Setting>

    @Query("SELECT *" +
            " FROM settings " +
            "WHERE visible = 1")
    fun getAll(): Flow<List<Setting>>
}
