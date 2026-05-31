package com.adityasuhag.eventplannerapp.viewmodel

import androidx.lifecycle.LiveData
import com.adityasuhag.eventplannerapp.data.Event
import com.adityasuhag.eventplannerapp.data.EventDao

class EventRepository(private val eventDao: EventDao) {

    val allEvents: LiveData<List<Event>> = eventDao.getAllEvents()

    suspend fun getEventById(id: Long): Event? = eventDao.getEventById(id)

    suspend fun insert(event: Event): Long = eventDao.insertEvent(event)

    suspend fun update(event: Event) = eventDao.updateEvent(event)

    suspend fun delete(event: Event) = eventDao.deleteEvent(event)
}