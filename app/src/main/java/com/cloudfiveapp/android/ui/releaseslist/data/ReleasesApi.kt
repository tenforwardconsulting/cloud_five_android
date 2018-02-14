package com.cloudfiveapp.android.ui.releaseslist.data

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface ReleasesApi {

    @GET("/products/{productId}/releases")
    fun getReleases(@Path("productId") productId: String): Flowable<List<Release>>
}
