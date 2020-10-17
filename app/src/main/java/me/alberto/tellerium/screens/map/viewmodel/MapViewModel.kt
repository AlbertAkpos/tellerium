package me.alberto.tellerium.screens.map.viewmodel

import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import me.alberto.tellerium.data.local.db.Farm
import me.alberto.tellerium.util.location.LocationState
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MapViewModel @Inject constructor() : ViewModel() {
    private val _locationState = MutableLiveData<LocationState>()
    val locationState: LiveData<LocationState> = _locationState

    private val _farmCoordinates = MutableLiveData<ArrayList<LatLng>>()
    val farmCoordinates: LiveData<ArrayList<LatLng>> = _farmCoordinates

    private val _farmAddress = MutableLiveData<String>()
    val farmAddress: LiveData<String> = _farmAddress

    private val farmLocation = MutableLiveData<Location>()

    private val _farm = MutableLiveData<Farm>()
    val farm: LiveData<Farm> = _farm


    fun updateFarmCoordinates(latLng: LatLng) {
        var current = _farmCoordinates.value
        if (current == null) {
            current = ArrayList()
        }
        current.add(latLng)
        _farmCoordinates.value = current
    }

    fun locationSetup(enabled: Boolean) {
        if (enabled) {
            _locationState.postValue(LocationState.TURNED_ON)
        } else {
            _locationState.postValue(LocationState.TURNED_OFF)
        }
    }

    fun setFarmAddress(context: Context, location: Location) {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addresses = geoCoder.getFromLocation(
            location.latitude,
            location.longitude, 1
        )
        _farmAddress.value = addresses[0].getAddressLine(0)
        farmLocation.value = location
    }

    fun clearValues() {
        _farmCoordinates.value = null
        _farmAddress.value = null
    }

    fun navigate() {
        val farm = Farm(
            _farmAddress.value.toString(),
            coordinates = _farmCoordinates.value!!,
            location = farmLocation.value!!
        )
        _farm.value = farm
    }

}