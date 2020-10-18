package me.alberto.tellerium.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.alberto.tellerium.util.FARM_DATABASE

@Database(entities = [FarmerEntity::class], version = 1)
@TypeConverters(Converter::class)
abstract class FarmerDatabase : RoomDatabase() {
    abstract val farmerDao: FarmerDao

    companion object {
        private lateinit var INSTANCE: FarmerDatabase
        fun getFarmDatabase(context: Context): FarmerDatabase {
            synchronized(FarmerDatabase::class) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        FarmerDatabase::class.java,
                        FARM_DATABASE
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}