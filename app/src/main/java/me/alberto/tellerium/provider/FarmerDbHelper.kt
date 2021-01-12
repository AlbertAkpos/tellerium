package me.alberto.tellerium.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.alberto.tellerium.data.local.db.FarmerDao
import me.alberto.tellerium.data.local.db.FarmerEntity
import me.alberto.tellerium.util.FARM_DATABASE
import me.alberto.tellerium.util.TABLE_NAME
import me.alberto.tellerium.util.extension.farmDatabase


class FarmerDbHelper(
    context: Context,
    nameOfDb: String = FARM_DATABASE,
    databaseVersion: Int
) : SQLiteOpenHelper(context, nameOfDb, null, databaseVersion) {


    override fun onCreate(db: SQLiteDatabase?) {
        val SQL_CREATE_FARM_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME(${FarmerProvider.ID} TEXT PRIMARY KEY NOT NULL, " +
                    "${FarmerProvider.ADDRESS} TEXT NOT NULL, " +
                    "${FarmerProvider.DOB} TEXT NOT NULL, " +
                    "${FarmerProvider.GENDER} TEXT NOT NULL, " +
                    "${FarmerProvider.IMAGE_URL} TEXT NOT NULL, " +
                    "${FarmerProvider.FARMS} TEXT, " +
                    "${FarmerProvider.NAME} TEXT NOT NULL)"

        db?.execSQL(SQL_CREATE_FARM_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        //TODO do code migration here
        onCreate(db)
    }
}