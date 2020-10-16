package me.alberto.tellerium.screens.newfarmer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.alberto.tellerium.data.domain.farmer.FarmerRepository
import me.alberto.tellerium.data.local.db.Farm
import me.alberto.tellerium.data.local.db.FarmerEntity
import timber.log.Timber
import javax.inject.Inject

class NewFarmerViewModel @Inject constructor(private val repository: FarmerRepository) :
    ViewModel() {
    private val _farms = MutableLiveData<List<Farm>>()
    val farms: LiveData<List<Farm>> = _farms

    val name = MutableLiveData<String>()
    val gender = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val dob = MutableLiveData<String>()
    val image = MutableLiveData<String>()


    fun onAddFarmer() {
        if (!verify()) return
        viewModelScope.launch {
            try {
                val farmer = FarmerEntity(
                    imageUrl = image.value!!,
                    address = address.value!!,
                    dob = dob.value!!,
                    gender = gender.value!!,
                    name = name.value!!,
                    farms = _farms.value
                )
                repository.addFarmer(farmer)
            } catch (exp: Exception) {
                Timber.d("Error: ${exp.message}")
            }
        }
    }

    private fun verify(): Boolean {
        if (name.value.isNullOrEmpty() || gender.value.isNullOrEmpty() || address.value.isNullOrEmpty()
            || dob.value.isNullOrEmpty() || image.value.isNullOrEmpty()
        ) {
            return false
        }
        return true
    }

    fun setImage(imgSrc: String) {
        image.value = imgSrc
    }

    fun setDate(date: String) {
        dob.value = date
    }

    fun editFarmer(farmerToEdit: FarmerEntity?) {
        farmerToEdit?.let {
            setImage(farmerToEdit.imageUrl)
            setDate(farmerToEdit.dob)
            name.value = farmerToEdit.name
            _farms.value = farmerToEdit.farms
            address.value = farmerToEdit.address
            gender.value = farmerToEdit.gender
        }
    }
}