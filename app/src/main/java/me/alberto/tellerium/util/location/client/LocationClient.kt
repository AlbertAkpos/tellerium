package me.alberto.tellerium.util.location.client

import android.location.Location
import androidx.annotation.CallSuper
import me.alberto.tellerium.util.location.LocationParams

abstract class LocationClient {
    private val locationClientListeners = mutableListOf<LocationClientListener>()

    abstract fun getLastLocation(locationClientListener: LocationClientListener)

    abstract fun observeLocation(locationClientListener: LocationClientListener, locationParams: LocationParams)

    @CallSuper
    open fun cleanUp() {
        locationClientListeners.clear()
    }

    fun notifyListenersOfLocation(location: Location) {
        locationClientListeners.forEach { it.onLocation(location) }
    }

    fun addListener(locationClientListener: LocationClientListener) {
        if (locationClientListeners.contains(locationClientListener).not()) {
            locationClientListeners.add(locationClientListener)
        }
    }
}