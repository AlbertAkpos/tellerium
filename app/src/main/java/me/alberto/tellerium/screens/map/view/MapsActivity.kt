package me.alberto.tellerium.screens.map.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.snackbar.Snackbar
import me.alberto.tellerium.App
import me.alberto.tellerium.R
import me.alberto.tellerium.data.local.db.Farm
import me.alberto.tellerium.databinding.ActivityMapsBinding
import me.alberto.tellerium.screens.common.base.BaseActivity
import me.alberto.tellerium.screens.map.viewmodel.MapViewModel
import me.alberto.tellerium.screens.newfarmer.view.MAP_RC
import me.alberto.tellerium.util.location.LocationEnabledListener
import me.alberto.tellerium.util.location.LocationParams
import me.alberto.tellerium.util.location.LocationSettingsUtils
import me.alberto.tellerium.util.location.LocationState
import me.alberto.tellerium.util.location.client.FusedLocationClient
import me.alberto.tellerium.util.location.client.LocationClientListener
import permissions.dispatcher.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

@RuntimePermissions
class MapsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var binding: ActivityMapsBinding

    private val gpsUtil by lazy {
        LocationSettingsUtils(this, gpsListener)
    }

    private val polylines = ArrayList<Polyline>()
    private val markers = ArrayList<Marker>()

    private val farm by lazy {
        intent?.getParcelableExtra<Farm>(FARM)
    }

    private lateinit var locationClient: FusedLocationClient

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel by viewModels<MapViewModel> { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        initView()
        turnOnLocationWithPermissionCheck()
    }

    @SuppressLint("MissingPermission")
    private fun initLocationClient() {
        if (this::locationClient.isInitialized) {
            locationClient.cleanUp()
        }

        locationClient = FusedLocationClient(this)
        val locationParams = LocationParams(100_000, 10.0)
        locationClient.observeLocation(locationListener, locationParams)
    }

    private fun setupObservers() {
        viewModel.locationState.observe(this, {
            if (it == LocationState.TURNED_ON) {
                initLocationClient()
                setupClickListeners()
                showMessage(binding.root, "Turned on")
            }
        })

        viewModel.farmCoordinates.observe(this, { list ->
            if (list.isNullOrEmpty()) {
                binding.captureBtn.isEnabled = false
                binding.clear.isEnabled = false
                return@observe
            }
            dropMarkers(list)

            if (list.size >= 2) {
                drawPolyline(list)
                binding.clear.isEnabled = true
            }

            if (list.size > 3) {
                binding.captureBtn.isEnabled = true
            }
        })

        viewModel.farm.observe(this, {
            it ?: return@observe
            val intent = Intent()
            intent.putExtra(FARM, it)
            setResult(MAP_RC, intent)
            finish()
        })
    }

    private fun dropMarkers(list: ArrayList<LatLng>) {
        for (marker in list) {
            val markerOptions = MarkerOptions().position(marker)
            val marker = mMap.addMarker(markerOptions)
            markers.add(marker)
        }
    }

    private fun drawPolyline(list: ArrayList<LatLng>) {
        val polylineOptions = PolylineOptions()
        for (latLng in list) {
            polylineOptions.add(latLng)
        }
        polylineOptions.color(Color.RED)
        val polyline = mMap.addPolyline(polylineOptions)
        polylines.add(polyline)
    }

    private fun setupClickListeners() {
        mMap.setOnMapClickListener { latLng ->
            viewModel.updateFarmCoordinates(latLng)
        }

        binding.captureBtn.setOnClickListener {
            drawFarmPolygon(viewModel.farmCoordinates.value!!)
            showMessage(binding.root, "Capturing farm")
            Timer().schedule(1000) {
                Handler(Looper.getMainLooper()).post {
                    viewModel.navigate()
                }
            }
        }
        binding.clear.setOnClickListener { clearMap() }
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        Places.initialize(applicationContext, getString(R.string.map_key))
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val autocompleteFragment = supportFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG
            )
        )

        autocompleteFragment.setOnPlaceSelectedListener(placesSelectedListener)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap = googleMap
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        setupObservers()
    }

    private fun clearMap() {
        //mMap.clear()
        polylines.forEach { it.remove() }
        markers.forEach { it.remove() }
        viewModel.clearValues()
    }

    private val gpsListener = object :
        LocationEnabledListener {
        override fun onGpsStatus(isGpsEnabled: Boolean) {
            viewModel.locationSetup(isGpsEnabled)
        }

        override fun onGpsError(message: String) {
            showMessage(binding.root, message)
            openLocationSettings()
        }
    }

    @NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun turnOnLocation() {
        gpsUtil.turnGpsOn()
    }

    @OnShowRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun showPermissionRequest(request: PermissionRequest) {
        showPermissionsRationale(request)
    }

    @OnPermissionDenied(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun showLocationPermissionDenied() {
        showMessage(binding.root, getString(R.string.permission_denied_rationale))
    }

    @OnNeverAskAgain(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun onNeverAskForLocationPermissionAgain() {
        val snackbar = Snackbar.make(
            binding.root,
            getString(R.string.needs_location_to_work),
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("Enable") {
            openLocationSettings()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        gpsUtil.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private val locationListener = object : LocationClientListener {
        override fun onLocation(location: Location) {
            if (farm == null) {
                moveDeviceCamera(location)
                viewModel.setFarmAddress(this@MapsActivity, location)
            } else {
                farm?.let {
                    moveDeviceCamera(it.location)
                    drawFarmPolygon(it.coordinates)
                }
            }
        }

        override fun onLocationError(exception: Exception) {
            Timber.d(exception)
        }
    }


    private val placesSelectedListener = object : PlaceSelectionListener {
        override fun onPlaceSelected(place: Place) {
            place.latLng?.let {
                val location = Location(LocationManager.GPS_PROVIDER)
                location.latitude = it.latitude
                location.longitude = it.longitude
                moveDeviceCamera(location, true)
                locationClient.cleanUp()
                viewModel.setFarmAddress(this@MapsActivity, location)
            }
        }

        override fun onError(p0: Status) {
            Timber.d("No location picked")
        }
    }

    private fun drawFarmPolygon(coordinates: List<LatLng>) {
        val polygonOptions = PolygonOptions().addAll(coordinates).clickable(true)
        val polygon = mMap.addPolygon(polygonOptions)
        polygon.fillColor = Color.GREEN

        showMessage(binding.root, "cp")
    }

    @Suppress("missingPermission")
    private fun moveDeviceCamera(location: Location, fromSearch: Boolean = false) {
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(location.latitude, location.longitude),
                14.0F
            )
        )
        if (fromSearch) {
            val latLng = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(latLng))
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15F))
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
    }


    companion object {
        const val FARM = "farm"
    }
}