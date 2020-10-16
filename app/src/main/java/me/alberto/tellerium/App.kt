package me.alberto.tellerium

import android.app.Application
import me.alberto.tellerium.di.component.AppComponent
import me.alberto.tellerium.di.component.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}