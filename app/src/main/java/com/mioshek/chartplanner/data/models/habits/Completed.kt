package com.mioshek.chartplanner.data.models.habits

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "completed",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = arrayOf("hId"),
            childColumns = arrayOf("hId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Completed(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val hId: Int,
    val date: Int,
    val time: Int
)