package me.alberto.tellerium.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.alberto.tellerium.screens.dashboard.viewmodel.DashboardViewModel
import me.alberto.tellerium.screens.login.viewmodel.LoginViewModel
import me.alberto.tellerium.screens.map.viewmodel.MapViewModel
import me.alberto.tellerium.screens.newfarmer.viewmodel.NewFarmerViewModel

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(dashboardViewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewFarmerViewModel::class)
    abstract fun bindNewFarmerViewModel(newFarmerViewModel: NewFarmerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindMapViewModel(mapViewModel: MapViewModel): ViewModel
}