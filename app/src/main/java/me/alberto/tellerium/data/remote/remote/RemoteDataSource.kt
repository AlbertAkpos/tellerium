package me.alberto.tellerium.data.remote.remote

import me.alberto.tellerium.data.local.db.FarmerEntity

interface RemoteDataSource {
    suspend fun getFarmers(): List<FarmerEntity>
}