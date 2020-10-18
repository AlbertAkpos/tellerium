package me.alberto.tellerium.screens.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.alberto.tellerium.data.domain.farmer.FarmerRepository
import me.alberto.tellerium.data.domain.usecase.GetFarmersUseCase
import me.alberto.tellerium.data.local.db.FarmerEntity
import timber.log.Timber
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val getFarmersUseCase: GetFarmersUseCase,
    private val farmerRepository: FarmerRepository
) :
    ViewModel() {

    private val _farmers = MutableLiveData<List<FarmerEntity>>()
    val farmers: LiveData<List<FarmerEntity>> = _farmers
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> =_errorMessage

    fun getFarmers() {
        viewModelScope.launch {
            try {
                updateValues()
                getFarmersUseCase.execute()
                val farmers = farmerRepository.getFarmers()
                _farmers.value = farmers
            } catch (exp: Exception) {
                Timber.d(exp.message.toString())
                updateValues()
                _errorMessage.postValue("Could'nt update from remote")
            }
        }
    }


    fun onDeleteFarmer(farmer: FarmerEntity) {
        viewModelScope.launch {
            try {
                farmerRepository.deleteFarmer(farmer)
                updateValues()
            } catch (exp: Exception) {
                Timber.d("Error deleting: ${exp.message}")
            }
        }
    }

    private fun updateValues() {
        viewModelScope.launch {
            try {
                _farmers.value = farmerRepository.getFarmers()
            } catch (exp: Exception) {
                Timber.d("Error: ${exp.message}")
            }
        }
    }


}