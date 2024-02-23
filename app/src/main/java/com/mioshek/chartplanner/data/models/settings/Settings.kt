package com.mioshek.chartplanner.data.models.settings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Setting(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val settingName: String,
    val value: String
)