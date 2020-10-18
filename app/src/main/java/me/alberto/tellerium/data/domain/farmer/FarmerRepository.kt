package me.alberto.tellerium.data.domain.farmer

import androidx.lifecycle.LiveData
import me.alberto.tellerium.data.local.db.FarmerEntity

interface FarmerRepository {
    suspend fun getRemoteFarmers()
    suspend fun getFarmers(): List<FarmerEntity>
    suspend fun deleteFarmer(vararg farmer: FarmerEntity)
    suspend fun updateFarmer(vararg farmer: FarmerEntity)
    suspend fun addFarmer(vararg farmer: FarmerEntity)
}