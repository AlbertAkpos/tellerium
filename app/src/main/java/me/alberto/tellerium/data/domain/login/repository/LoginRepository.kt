package me.alberto.tellerium.data.domain.repository

interface LoginRepository {
    suspend fun setLogin(logIn: Boolean)
    suspend fun clearLogin()
    suspend fun getLogin(): Boolean
}