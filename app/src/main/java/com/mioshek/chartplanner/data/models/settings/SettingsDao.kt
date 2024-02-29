package com.mioshek.chartplanner.data.models.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao{

    @Insert
    suspend fun insert(setting: Setting)

    @Query("DELETE FROM settings WHERE settingName = :settingName")
    suspend fun delete(settingName: String)

    @Query("SELECT * " +
            "FROM settings " +
            "WHERE settingName = :settingName")
    fun getSetting(settingName: String): Setting

    @Query("INSERT OR IGNORE INTO settings (settingName, value)" +
            "VALUES (:settingName, :value);")
    suspend fun update(settingName: String, value: String)

    @Query("SELECT * FROM settings")
    fun getAll(): Flow<List<Setting>>
}