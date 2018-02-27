package com.cloudfiveapp.android.ui.productslist.data

import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET

interface ProductsApi {

    @GET("/products")
    fun getProducts(): Single<Result<List<Product>>>
}
