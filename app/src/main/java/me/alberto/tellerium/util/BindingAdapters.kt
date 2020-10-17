package me.alberto.tellerium.util

import android.view.LayoutInflater
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import me.alberto.tellerium.R
import me.alberto.tellerium.data.local.db.Farm
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
fun LinearLayout.addFarms(farmsList: List<Farm>?) {
    val linearLayout = this
    val inflater = LayoutInflater.from(linearLayout.context)
    val farms = farmsList?.map { farmItem ->
        val farm = inflater.inflate(R.layout.farm_item, linearLayout, false) as TextView
        farm.text = farmItem.name
        farm
    }

    linearLayout.removeAllViews()

    farms?.let {
        for (btn in it) {
            linearLayout.addView(btn)
        }
    }
}

@BindingAdapter("app:data")
fun AutoCompleteTextView.setGender(list: List<String>?) {
    val adapter =
        ArrayAdapter(context, R.layout.farm_item, list ?: listOf())
    setAdapter(adapter)
}


