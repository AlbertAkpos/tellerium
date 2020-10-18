package me.alberto.tellerium.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import me.alberto.tellerium.data.local.db.FarmerDatabase
import me.alberto.tellerium.util.DomainDateFormater
import me.alberto.tellerium.util.PREF_NAME

@Module
class LocalModule {
    @Provides
    fun getSharedPrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val pref = EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return pref
    }

    @Provides
    fun provideFarmerDatabase(context: Context): FarmerDatabase {
        return FarmerDatabase.getFarmDatabase(context)
    }

    @Provides
    fun provideDateFormatter(): DomainDateFormater {
        return DomainDateFormater()
    }
}