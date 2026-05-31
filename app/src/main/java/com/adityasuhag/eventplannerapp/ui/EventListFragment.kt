package com.adityasuhag.eventplannerapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adityasuhag.eventplannerapp.R
import com.adityasuhag.eventplannerapp.viewmodel.EventViewModel
import com.google.android.material.snackbar.Snackbar

class EventListFragment : Fragment() {

    private val viewModel: EventViewModel by viewModels()
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_event_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerEvents)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)

        adapter = EventAdapter(
            onItemClick = { event ->
                val bundle = Bundle().apply { putLong("eventId", event.id) }
                findNavController().navigate(R.id.action_eventList_to_addEdit, bundle)
            },
            onDeleteClick = { event ->
                viewModel.delete(event)
                Snackbar.make(view, "Event deleted: ${event.title}", Snackbar.LENGTH_SHORT)
                    .setAction("UNDO") { viewModel.insert(event) }
                    .show()
            }
        )

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        viewModel.allEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
            tvEmpty.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}