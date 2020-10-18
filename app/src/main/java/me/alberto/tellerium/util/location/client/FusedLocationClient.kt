package me.alberto.tellerium.util.location.client

import android.content.Context
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import me.alberto.tellerium.util.location.LocationParams
import timber.log.Timber

class FusedLocationClient(context: Context) : LocationClient() {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            Timber.d("Location gotten: $locationResult")
            locationResult ?: return
            notifyListenersOfLocation(locationResult.lastLocation)
        }
    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    override fun getLastLocation(locationClientListener: LocationClientListener) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    locationClientListener.onLocation(location)
                } else {
                    locationClientListener.onLocationError(NullPointerException("Location is null"))
                }

            }.addOnFailureListener { locationClientListener.onLocationError(it) }
    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    override fun observeLocation(
        locationClientListener: LocationClientListener,
        locationParams: LocationParams
    ) {
        addListener(locationClientListener)
        val locationRequest = LocationRequest.create()
        with(locationRequest) {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = locationParams.interval
            fastestInterval = locationParams.interval
            smallestDisplacement = locationParams.distance.toFloat()
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun cleanUp() {
        super.cleanUp()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}