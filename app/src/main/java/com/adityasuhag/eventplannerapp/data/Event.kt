package com.adityasuhag.eventplannerapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// Single event row in the local Room database.
// id = 0 because Room treats 0 as "not yet assigned" and autogenerates one on insert.
@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String,
    val location: String,
    val dateTime: Date
)









