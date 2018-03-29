package com.cloudfiveapp.android.data.remote

import com.cloudfiveapp.android.data.model.Release
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ReleasesApi {

    @GET("/products/{productId}/releases")
    fun getReleases(@Path("productId") productId: String): Call<List<Release>>
}
