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
    @PrimaryKey
    @NonNull
    val id: Int,

    @NonNull
    val habitId: Int,

    @NonNull
    val date: Long,

    @NonNull
    val completed: Boolean
)