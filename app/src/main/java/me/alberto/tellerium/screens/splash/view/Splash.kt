package me.alberto.tellerium.screens.splash.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.runBlocking
import me.alberto.tellerium.App
import me.alberto.tellerium.R
import me.alberto.tellerium.data.domain.usecase.GetLoginUseCase
import me.alberto.tellerium.screens.dashboard.view.DashboardActivity
import me.alberto.tellerium.screens.login.view.LoginActivity
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class Splash : AppCompatActivity() {
    @Inject
    lateinit var getLoginUseCase: GetLoginUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Timer().schedule(2000) {
            checkLoginStatus()
        }
    }

    private fun checkLoginStatus() {
        runBlocking {
            val loggedIn = getLoginUseCase.execute()
            if (loggedIn) {
                startActivity(Intent(this@Splash, DashboardActivity::class.java))
            } else {
                startActivity(Intent(this@Splash, LoginActivity::class.java ))
            }
            finish()
        }
    }
}