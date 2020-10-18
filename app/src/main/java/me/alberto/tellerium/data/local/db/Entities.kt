package me.alberto.tellerium.data.local.db

import android.location.Location
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import me.alberto.tellerium.util.TABLE_NAME
import java.util.*

@Entity(tableName = TABLE_NAME)
@Parcelize
data class FarmerEntity(
    @PrimaryKey
    val id: String,
    val imageUrl: String,
    val name: String,
    val gender: String,
    val address: String,
    val dob: String,
    val farms: List<Farm>? = null
) : Parcelable

@Parcelize
data class Farm(
    val name: String,
    val location: Location,
    val coordinates: List<LatLng>
) : Parcelable

//@Parcelize
//data class FarmLocation(
//    var lat: Double,
//    var long: Double
//) : Parcelable {
//    constructor() : this(0.0, 0.0)
//}


class Converter {
    private val gson = Gson()

    @TypeConverter
    fun fromFarmLocationList(coordinates: List<LatLng>): String {
        val type = object : TypeToken<List<LatLng>>() {}.type
        return gson.toJson(coordinates, type)
    }

    @TypeConverter
    fun toFarmLocationList(string: String): List<LatLng> {
        val type = object : TypeToken<List<LatLng>>() {}.type
        return gson.fromJson(string, type)
    }

    @TypeConverter
    fun fromFarmList(farms: List<Farm>?): String? {
        val type = object : TypeToken<List<Farm>>() {}.type
        return gson.toJson(farms, type) ?: null
    }

    @TypeConverter
    fun toFarmList(string: String): List<Farm>? {
        val type = object : TypeToken<List<Farm>>() {}.type
        return gson.fromJson(string, type)
    }

}