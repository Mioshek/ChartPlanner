package com.mioshek.chartplanner.data.models.habits

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "completed",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = arrayOf("hid"),
            childColumns = arrayOf("habitId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Completed(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val habitId: Int,

    val date: String,
)