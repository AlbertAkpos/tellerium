package me.alberto.tellerium.data.remote.api

import me.alberto.tellerium.data.remote.model.ApiResult
import me.alberto.tellerium.util.Urls
import retrofit2.http.GET

interface RemoteService {
    @GET(Urls.FIRST_20_FARMERS)
    suspend fun getFarmers(): ApiResult
}