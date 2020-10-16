package me.alberto.tellerium.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import me.alberto.tellerium.di.module.DataModule
import me.alberto.tellerium.di.module.LocalModule
import me.alberto.tellerium.di.viewmodel.ViewModelModule
import me.alberto.tellerium.screens.dashboard.view.DashboardActivity
import me.alberto.tellerium.screens.login.view.LoginActivity
import me.alberto.tellerium.screens.splash.view.Splash

@Component(modules = [DataModule::class, LocalModule::class, ViewModelModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: LoginActivity)
    fun inject(activity: Splash)
    fun inject(activity: DashboardActivity)
}
