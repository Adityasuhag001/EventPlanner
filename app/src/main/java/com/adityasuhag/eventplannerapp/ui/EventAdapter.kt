package com.adityasuhag.eventplannerapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adityasuhag.eventplannerapp.R
import com.adityasuhag.eventplannerapp.data.Event
import java.text.SimpleDateFormat
import java.util.Locale

// Comparator for ListAdapter so RecyclerView knows which rows actually changed.
private val EVENT_DIFF = object : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}

class EventAdapter(
    private val onItemClick: (Event) -> Unit,
    private val onDeleteClick: (Event) -> Unit
) : ListAdapter<Event, EventAdapter.EventVH>(EVENT_DIFF) {

    private val dateFmt = SimpleDateFormat("EEE, dd MMM yyyy • hh:mm a", Locale.getDefault())

    class EventVH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvLocation: TextView = view.findViewById(R.id.tvLocation)
        val tvDateTime: TextView = view.findViewById(R.id.tvDateTime)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventVH(v)
    }

    override fun onBindViewHolder(holder: EventVH, position: Int) {
        val ev = getItem(position)
        holder.tvTitle.text = ev.title
        holder.tvCategory.text = ev.category
        holder.tvLocation.text = if (ev.location.isBlank()) "No location" else ev.location
        holder.tvDateTime.text = dateFmt.format(ev.dateTime)

        holder.itemView.setOnClickListener { onItemClick(ev) }
        holder.btnDelete.setOnClickListener { onDeleteClick(ev) }
    }
}