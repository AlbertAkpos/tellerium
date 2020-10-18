package me.alberto.tellerium.data.domain.login.repository

import me.alberto.tellerium.data.domain.repository.LoginRepository
import me.alberto.tellerium.data.local.source.LoginDataSource
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val local: LoginDataSource.Local) :
    LoginRepository {
    override suspend fun setLogin(logIn: Boolean) {
        local.setLogin(logIn)
    }

    override suspend fun clearLogin() {
        local.clearLogin()
    }

    override suspend fun getLogin(): Boolean {
        return local.getLogin()
    }
}