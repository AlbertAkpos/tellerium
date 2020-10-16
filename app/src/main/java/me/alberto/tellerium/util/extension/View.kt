package me.alberto.tellerium.util.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import me.alberto.tellerium.R

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun AutoCompleteTextView.setGender() {
    val texts = listOf("Male", "Female")
    val adapter =
        ArrayAdapter(context, R.layout.farm_item, texts)
    setAdapter(adapter)
}