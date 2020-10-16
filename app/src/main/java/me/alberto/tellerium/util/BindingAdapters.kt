package me.alberto.tellerium.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import me.alberto.tellerium.data.domain.model.Farmer
import me.alberto.tellerium.data.local.db.FarmerEntity
import me.alberto.tellerium.screens.dashboard.adapter.FarmerAdapter

@BindingAdapter("app:errorMessage")
fun TextInputLayout.showError(errorMessage: String?) {
    if (error != errorMessage) {
        isErrorEnabled = true
        error = errorMessage
    }
}

@BindingAdapter("app:imageSrc")
fun ImageView.loadImageFromUrl(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}

@BindingAdapter("app:farmers")
fun RecyclerView.loadFarmers(list: List<FarmerEntity>?) {
    val adapter = adapter as FarmerAdapter
    adapter.submitList(list)
}