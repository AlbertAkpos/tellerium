package me.alberto.tellerium.data.remote

import com.google.gson.GsonBuilder
import me.alberto.tellerium.BuildConfig
import me.alberto.tellerium.data.remote.api.RemoteService
import me.alberto.tellerium.util.Urls
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RestClient @Inject constructor(
) {

    companion object {
        private const val TIMEOUT: Long = 50
        private val gson = GsonBuilder().create()
    }

    private val api: RemoteService

    init {
        val loggingInterceptor = makeLoggingInterceptor(BuildConfig.DEBUG)

        val httpClient = OkHttpClient.Builder()
            .apply {
                connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                readTimeout(TIMEOUT, TimeUnit.SECONDS)

                addInterceptor(loggingInterceptor)
            }

        val client = httpClient.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Urls.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        api = retrofit.create(RemoteService::class.java)
    }

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }

    fun getRemoteCaller() = api
}