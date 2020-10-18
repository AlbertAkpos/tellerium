package me.alberto.tellerium.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import me.alberto.tellerium.data.remote.RestClient

@Module
class NetworkModul {


    @Provides
    fun restClient(): RestClient {
        return RestClient()
    }
}