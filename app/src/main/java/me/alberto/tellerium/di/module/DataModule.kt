package me.alberto.tellerium.di.module

import dagger.Binds
import dagger.Module
import me.alberto.tellerium.data.domain.farmer.FarmerRepository
import me.alberto.tellerium.data.domain.farmer.FarmerRepositoryImpl
import me.alberto.tellerium.data.domain.login.repository.LoginRepositoryImpl
import me.alberto.tellerium.data.domain.repository.LoginRepository
import me.alberto.tellerium.data.local.source.LoginDataSource
import me.alberto.tellerium.data.local.source.LoginLocalDataSource
import me.alberto.tellerium.data.remote.remote.RemoteDataSource
import me.alberto.tellerium.data.remote.remote.RemoteDataSourceImpl

@Module
abstract class DataModule {
    @Binds
    abstract fun provideLoginLocalDataSource(loginLocalDataSource: LoginLocalDataSource): LoginDataSource.Local

    @Binds
    abstract fun provideLoginRepositoryImpl(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun provideRemoteDataSourceImpl(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    abstract fun provideFarmerRepositoryImpl(farmerRepositoryImpl: FarmerRepositoryImpl): FarmerRepository
}