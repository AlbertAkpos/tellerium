package me.alberto.tellerium.data.local

import android.content.SharedPreferences

/**
 * Created by kryptkode on 10/23/2019.
 */

abstract class BasePreferencesManager(protected val sharedPreferences: SharedPreferences) {

    protected val loginDataKey = "prefLoginData"

    protected fun setStringPreference(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    protected fun getStringPreference(
        key: String
    ): String? {
        return sharedPreferences.getString(key, null)
    }


    protected fun getBooleanPreference(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    protected fun setBooleanPreference(key: String, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    protected fun resetPreferences() {
        sharedPreferences.edit().clear().apply()
    }

}