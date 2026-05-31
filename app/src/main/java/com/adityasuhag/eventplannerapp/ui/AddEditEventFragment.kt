package com.adityasuhag.eventplannerapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adityasuhag.eventplannerapp.R
import com.adityasuhag.eventplannerapp.data.Event
import com.adityasuhag.eventplannerapp.viewmodel.EventViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEditEventFragment : Fragment() {

    private val viewModel: EventViewModel by viewModels()

    // Holds the user's chosen date+time as they tap the pickers.
    private val pickedCal: Calendar = Calendar.getInstance()
    private var hasDate = false
    private var hasTime = false

    // -1 means we're adding a new event, otherwise we're editing this id.
    private var editId: Long = -1L

    private val displayFmt = SimpleDateFormat("EEE, dd MMM yyyy • hh:mm a", Locale.getDefault())

    private val categoryList = listOf("Work", "Social", "Travel", "Personal", "Health", "Other")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTitle = view.findViewById<TextInputEditText>(R.id.etTitle)
        val etLocation = view.findViewById<TextInputEditText>(R.id.etLocation)
        val spinner = view.findViewById<Spinner>(R.id.spinnerCategory)
        val dateBtn = view.findViewById<Button>(R.id.btnPickDate)
        val timeBtn = view.findViewById<Button>(R.id.btnPickTime)
        val dtLabel = view.findViewById<TextView>(R.id.tvSelectedDateTime)
        val saveBtn = view.findViewById<Button>(R.id.btnSave)

        spinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categoryList
        )

        // If we got an eventId arg, this is edit mode — load the existing event.
        editId = arguments?.getLong("eventId", -1L) ?: -1L
        if (editId != -1L) {
            lifecycleScope.launch {
                val existing = viewModel.getEventById(editId)
                if (existing != null) {
                    etTitle.setText(existing.title)
                    etLocation.setText(existing.location)

                    var idx = categoryList.indexOf(existing.category)
                    if (idx < 0) idx = 0
                    spinner.setSelection(idx)

                    pickedCal.time = existing.dateTime
                    hasDate = true
                    hasTime = true
                    dtLabel.text = displayFmt.format(existing.dateTime)
                }
            }
        }

        dateBtn.setOnClickListener {
            showDatePicker(dtLabel)
        }

        timeBtn.setOnClickListener {
            showTimePicker(dtLabel)
        }

        saveBtn.setOnClickListener {
            handleSave(view, etTitle, etLocation, spinner)
        }
    }

    private fun showDatePicker(dtLabel: TextView) {
        val today = Calendar.getInstance()
        val dlg = DatePickerDialog(
            requireContext(),
            { _, y, m, d ->
                pickedCal.set(Calendar.YEAR, y)
                pickedCal.set(Calendar.MONTH, m)
                pickedCal.set(Calendar.DAY_OF_MONTH, d)
                hasDate = true
                refreshDateTimeLabel(dtLabel)
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )
        // Block any date before today at the OS level.
        dlg.datePicker.minDate = today.timeInMillis
        dlg.show()
    }

    private fun showTimePicker(dtLabel: TextView) {
        val now = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, h, min ->
                pickedCal.set(Calendar.HOUR_OF_DAY, h)
                pickedCal.set(Calendar.MINUTE, min)
                pickedCal.set(Calendar.SECOND, 0)
                hasTime = true
                refreshDateTimeLabel(dtLabel)
            },
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun handleSave(
        view: View,
        etTitle: TextInputEditText,
        etLocation: TextInputEditText,
        spinner: Spinner
    ) {
        val title = etTitle.text?.toString()?.trim() ?: ""
        val location = etLocation.text?.toString()?.trim() ?: ""
        val category = spinner.selectedItem?.toString() ?: "Other"

        if (title.isEmpty()) {
            Snackbar.make(view, "Title is required", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (!hasDate || !hasTime) {
            Snackbar.make(view, "Please pick both date and time", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (pickedCal.timeInMillis <= System.currentTimeMillis()) {
            Snackbar.make(view, "Date/time must be in the future", Snackbar.LENGTH_SHORT).show()
            return
        }

        val newEvent = Event(
            id = if (editId == -1L) 0 else editId,
            title = title,
            category = category,
            location = location,
            dateTime = Date(pickedCal.timeInMillis)
        )

        if (editId == -1L) {
            viewModel.insert(newEvent)
            Snackbar.make(view, "Event saved", Snackbar.LENGTH_SHORT).show()
        } else {
            viewModel.update(newEvent)
            Snackbar.make(view, "Event updated", Snackbar.LENGTH_SHORT).show()
        }

        findNavController().popBackStack()
    }

    private fun refreshDateTimeLabel(tv: TextView) {
        if (hasDate && hasTime) {
            tv.text = displayFmt.format(pickedCal.time)
        } else if (hasDate) {
            val dateOnly = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
                .format(pickedCal.time)
            tv.text = "$dateOnly • (pick time)"
        }
    }
}