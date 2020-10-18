package me.alberto.tellerium.util.location

import android.content.Context
import android.location.LocationManager

class LocationEnabledChecker(context: Context) {
    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun isGpsTurnedOn(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}