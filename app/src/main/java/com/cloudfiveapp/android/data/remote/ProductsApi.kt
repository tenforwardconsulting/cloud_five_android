package com.cloudfiveapp.android.data.remote

import com.cloudfiveapp.android.data.model.Product
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET

interface ProductsApi {

    @GET("/products")
    fun getProducts(): Single<Result<List<Product>>>
}
