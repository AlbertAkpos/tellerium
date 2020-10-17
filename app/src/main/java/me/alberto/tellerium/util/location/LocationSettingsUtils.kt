package me.alberto.tellerium.util.location

import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import me.alberto.tellerium.R
import timber.log.Timber

class LocationSettingsUtils(
    private val activity: FragmentActivity,
    private val onLocationEnabledListener: LocationEnabledListener? = null
) {
    private val settingsClient: SettingsClient = LocationServices.getSettingsClient(activity)
    private val locationSettingsRequest: LocationSettingsRequest
    private val gpsStateChecker =
        LocationEnabledChecker(activity)
    private val locationRequest: LocationRequest = LocationRequest.create()

    fun isGpsTurnedOn(): Boolean {
        return gpsStateChecker.isGpsTurnedOn()
    }

    fun turnGpsOn() {
        if (isGpsTurnedOn()) {
            onLocationEnabledListener?.onGpsStatus(true)
        } else {
            settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(
                    (activity)
                ) {
                    //  GPS is already enable, callback GPS status through listener
                    onLocationEnabledListener?.onGpsStatus(true)
                }
                .addOnFailureListener(activity) { e ->
                    Timber.e(e)
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                activity,
                                GPS_REQUEST
                            )
                        } catch (sie: SendIntentException) {
                            Timber.e(sie, "PendingIntent unable to execute request.")
                            handleError()
                        }
                        else -> {
                            handleError()
                        }
                    }
                }
        }
    }

    private fun handleError() {
        val errorMessage =
            activity.getString(R.string.location_setup_inadequate)
        onLocationEnabledListener?.onGpsError(errorMessage)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GPS_REQUEST){
            onLocationEnabledListener?.onGpsStatus(resultCode == Activity.RESULT_OK)
        }
    }

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000.toLong()
        locationRequest.fastestInterval = 2 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        locationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)
    }

    companion object {
        private const val GPS_REQUEST = 1001
    }
}