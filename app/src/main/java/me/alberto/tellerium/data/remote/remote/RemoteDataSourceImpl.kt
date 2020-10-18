package me.alberto.tellerium.data.remote.remote

import me.alberto.tellerium.data.local.db.FarmerEntity
import me.alberto.tellerium.data.remote.RestClient
import me.alberto.tellerium.data.remote.model.toDomainFarm
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor (private val restClient: RestClient): RemoteDataSource {
    override suspend fun getFarmers(): List<FarmerEntity> {
        val result = restClient.getRemoteCaller().getFarmers()
        val farmers = result.data.farmers.toDomainFarm()
        return farmers
    }
}