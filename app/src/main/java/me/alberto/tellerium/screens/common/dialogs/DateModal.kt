package me.alberto.tellerium.screens.common.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DateModal : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var listener: DatePickerListener? = null

    companion object {
        fun newInstance(listener: DatePickerListener): DateModal {
            val fragment = DateModal()
            fragment.listener = listener
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(requireActivity(), this, year, month, day)
        datePicker.datePicker.maxDate = System.currentTimeMillis()
        return datePicker
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener?.onDatePicked(year, month, dayOfMonth)
    }

    interface DatePickerListener {
        fun onDatePicked(year: Int, month: Int, dayOfMonth: Int)
    }
}