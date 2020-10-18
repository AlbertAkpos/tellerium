package me.alberto.tellerium.data.local.source


interface LoginDataSource {
    interface Local {
        suspend fun setLogin(loggedIn: Boolean)
        suspend fun clearLogin()
        suspend fun getLogin(): Boolean
    }
}