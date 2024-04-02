package com.mioshek.chartplanner.data.models.settings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Setting(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val settingName: String,
    val value: String,
    val displayType: Int, // 0 No Display | 1 Slider | 2 Switch | 3 Dropdown List
    val visible: Int
)