package me.alberto.tellerium.data.local.db

import androidx.room.*

@Dao
interface FarmerDao {
    @Query("SELECT * from farmer_table")
    suspend fun getFarmers(): List<FarmerEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFarmer(vararg farmer: FarmerEntity)

    @Delete
    suspend fun deleteFarmer(vararg farmer: FarmerEntity)

    @Update
    suspend fun updateFarmer(vararg farmer: FarmerEntity)


}