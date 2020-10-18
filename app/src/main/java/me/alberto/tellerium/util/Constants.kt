package me.alberto.tellerium.util

import android.location.Location
import android.location.LocationManager

const val PREF_NAME = "shred_pref_name"
const val TABLE_NAME = "farmer_table"
const val FARM_DATABASE = "farm-database"

object Urls {
    const val IMAGE_BASE_URL = "https://s3-eu-west-1.amazonaws.com/agromall-storage/"
    const val BASE_URL = "http://theagromall.com/"
    const val FIRST_20_FARMERS = "api/v2/get-sample-farmers?limit=20"
}

fun makeLocation(latitude: Double, longitude: Double): Location {
    val location = Location(LocationManager.GPS_PROVIDER)
    location.latitude = latitude
    location.longitude = longitude
    return location
}