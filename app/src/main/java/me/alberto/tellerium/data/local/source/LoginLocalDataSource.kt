package me.alberto.tellerium.data.local.source

import android.content.SharedPreferences
import me.alberto.tellerium.data.local.BasePreferencesManager
import javax.inject.Inject

class LoginLocalDataSource @Inject constructor(
    sharedPreferences: SharedPreferences
) : BasePreferencesManager(sharedPreferences), LoginDataSource.Local {
    override suspend fun setLogin(loggedIn: Boolean) {
        setBooleanPreference(loginDataKey, loggedIn)
    }

    override suspend fun clearLogin() {
        resetPreferences()
    }

    override suspend fun getLogin(): Boolean {
        return getBooleanPreference(loginDataKey)
    }
}