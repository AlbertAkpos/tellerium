package me.alberto.tellerium.data.remote.model

import com.google.gson.annotations.SerializedName
import me.alberto.tellerium.data.domain.model.Farmer
import me.alberto.tellerium.data.local.db.FarmerEntity
import me.alberto.tellerium.util.Urls

data class ApiResult(
    val status: String,
    val data: Data
)

data class Data(
    val farmers: List<_Farmer>,
    val totalRec: Long,

    @SerializedName("imageBaseUrl")
    val imageBaseURL: String
)


data class _Farmer(
    @SerializedName("farmer_id")
    val farmerID: String,

    @SerializedName("reg_no")
    val regNo: String,

    val bvn: String,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("middle_name")
    val middleName: String,

    val surname: String,
    val dob: String,
    val gender: String,
    val nationality: String,
    val occupation: String,

    @SerializedName("marital_status")
    val maritalStatus: String,

    @SerializedName("spouse_name")
    val spouseName: String,

    val address: String,
    val city: String,
    val lga: String,
    val state: String,

    @SerializedName("mobile_no")
    val mobileNo: String,

    @SerializedName("email_address")
    val emailAddress: String,

    @SerializedName("id_type")
    val idType: String,

    @SerializedName("id_no")
    val idNo: String,

    @SerializedName("issue_date")
    val issueDate: String,

    @SerializedName("expiry_date")
    val expiryDate: String,

    @SerializedName("id_image")
    val idImage: String,

    @SerializedName("passport_photo")
    val passportPhoto: String,

    @SerializedName("fingerprint")
    val fingerprint: String
)

fun List<_Farmer>.toDomainFarm(): List<FarmerEntity> {
    return this.map {
        FarmerEntity(
            imageUrl = Urls.IMAGE_BASE_URL + it.passportPhoto,
            address = it.address,
            gender = it.gender,
            name = "${it.firstName} ${it.surname} ${it.middleName}",
            dob = it.dob,
            id = it.farmerID
        )
    }
}
