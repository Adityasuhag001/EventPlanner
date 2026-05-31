package com.adityasuhag.eventplannerapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.adityasuhag.eventplannerapp.data.AppDatabase
import com.adityasuhag.eventplannerapp.data.Event
import kotlinx.coroutines.launch

// ViewModel for the event list and add/edit screens.
// Uses AndroidViewModel because we need the application context to get the Room DB.
class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventRepository
    val allEvents: LiveData<List<Event>>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = EventRepository(db.eventDao())
        allEvents = repository.allEvents
    }

    fun insert(event: Event) {
        viewModelScope.launch {
            repository.insert(event)
        }
    }

    fun update(event: Event) {
        viewModelScope.launch {
            repository.update(event)
        }
    }

    fun delete(event: Event) {
        viewModelScope.launch {
            repository.delete(event)
        }
    }

    suspend fun getEventById(id: Long): Event? {
        return repository.getEventById(id)
    }
}