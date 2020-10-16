package me.alberto.tellerium.data.local.source

import me.alberto.tellerium.data.local.db.FarmerDatabase
import me.alberto.tellerium.data.local.db.FarmerEntity
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val farmerDatabase: FarmerDatabase) :
    LocalDataSource {
    override suspend fun getFarmers(): List<FarmerEntity> {
        return farmerDatabase.farmerDao.getFarmers()
    }

    override suspend fun addFarmer(vararg farmer: FarmerEntity) {
        farmerDatabase.farmerDao.addFarmer(*farmer)
    }

    override suspend fun updateFarmer(vararg farmer: FarmerEntity) {
        farmerDatabase.farmerDao.updateFarmer(*farmer)
    }

    override suspend fun deleteFarmer(vararg farmer: FarmerEntity) {
        farmerDatabase.farmerDao.deleteFarmer(*farmer)
    }
}