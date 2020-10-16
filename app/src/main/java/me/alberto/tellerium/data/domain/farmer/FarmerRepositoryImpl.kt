package me.alberto.tellerium.data.domain.farmer

import androidx.lifecycle.LiveData
import me.alberto.tellerium.data.local.db.FarmerDatabase
import me.alberto.tellerium.data.local.db.FarmerEntity
import me.alberto.tellerium.data.remote.remote.RemoteDataSource
import javax.inject.Inject

class FarmerRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val farmerDatabase: FarmerDatabase
) :
    FarmerRepository {
    @Suppress("unchecked_cast")
    override suspend fun getRemoteFarmers() {
        val remoteFarmers = remoteDataSource.getFarmers()
        farmerDatabase.farmerDao.addFarmer(*remoteFarmers.toTypedArray())
    }

    override suspend fun getFarmers(): List<FarmerEntity> {
        return farmerDatabase.farmerDao.getFarmers()
    }

    override suspend fun deleteFarmer(vararg farmer: FarmerEntity) {
        farmerDatabase.farmerDao.deleteFarmer(*farmer)
    }

    override suspend fun updateFarmer(vararg farmer: FarmerEntity) {
        farmerDatabase.farmerDao.updateFarmer(*farmer)
    }

    override suspend fun addFarmer(vararg farmer: FarmerEntity) {
        farmerDatabase.farmerDao.addFarmer(*farmer)
    }
}