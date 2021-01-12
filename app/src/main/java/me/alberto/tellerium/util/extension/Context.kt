package me.alberto.tellerium.util.extension

import android.content.Context
import me.alberto.tellerium.data.local.db.FarmerDao
import me.alberto.tellerium.data.local.db.FarmerDatabase

val Context.farmDatabase: FarmerDao get() = FarmerDatabase.getFarmDatabase(applicationContext).farmerDao