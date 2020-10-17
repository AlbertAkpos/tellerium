package me.alberto.tellerium.util.location.client

import android.location.Location
import java.lang.Exception

interface LocationClientListener {
    fun onLocation(location: Location)
    fun onLocationError(exception: Exception)
}