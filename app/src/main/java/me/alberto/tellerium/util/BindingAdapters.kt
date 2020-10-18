package me.alberto.tellerium.util

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import me.alberto.tellerium.R
import me.alberto.tellerium.data.local.db.Farm
import me.alberto.tellerium.data.local.db.FarmerEntity
import me.alberto.tellerium.screens.dashboard.adapter.FarmerAdapter
import me.alberto.tellerium.screens.newfarmer.adapter.FarmAdapter

@BindingAdapter("app:errorMessage")
fun TextInputLayout.showError(errorMessage: String?) {
    if (error != errorMessage) {
        isErrorEnabled = true
        error = errorMessage
    }
}

@BindingAdapter("app:imageSrc")
fun ImageView.loadImageFromUrl(url: String?) {
    url?.let {
        Glide.with(context)
            .load(url)
            .into(this)
    }
}

@BindingAdapter("app:farmers")
fun RecyclerView.loadFarmers(list: List<FarmerEntity>?) {
    val adapter = adapter as FarmerAdapter
    adapter.submitList(list)
}

@BindingAdapter("app:farms")
fun RecyclerView.addFarms(farmsList: List<Farm>?) {
    val adapter = adapter as FarmAdapter
    adapter.submitList(farmsList)
}

@BindingAdapter("app:data")
fun AutoCompleteTextView.setGender(list: List<String>?) {
    val adapter =
        ArrayAdapter(context, R.layout.simple_text_item, list ?: listOf("Male", "Female"))
    setAdapter(adapter)
}


