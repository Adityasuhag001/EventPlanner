package com.adityasuhag.eventplannerapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Event::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        // Volatile so changes to INSTANCE are visible across threads immediately.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Return existing instance if we already built one.
            val existing = INSTANCE
            if (existing != null) return existing

            // Otherwise build it, but lock so two threads don't both build at once.
            synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "event_planner_db"
                ).build()
                INSTANCE = newInstance
                return newInstance
            }
        }
    }
}