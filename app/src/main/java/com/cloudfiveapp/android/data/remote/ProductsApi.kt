package com.cloudfiveapp.android.data.remote

import com.cloudfiveapp.android.data.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface ProductsApi {

    @GET("/products")
    fun getProducts(): Call<List<Product>>
}
