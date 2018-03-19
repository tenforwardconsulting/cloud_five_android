package com.cloudfiveapp.android.data.remote

import com.cloudfiveapp.android.data.model.Release
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Path

interface ReleasesApi {

    @GET("/products/{productId}/releases")
    fun getReleases(@Path("productId") productId: String): Single<Result<List<Release>>>
}
