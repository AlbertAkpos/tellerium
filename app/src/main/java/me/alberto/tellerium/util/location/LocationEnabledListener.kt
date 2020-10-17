package me.alberto.tellerium.util.location

interface LocationEnabledListener {
    fun onGpsStatus(isGpsEnabled: Boolean)
    fun onGpsError(message: String)
}