package com.cloudfiveapp.android.data.remote

import com.cloudfiveapp.android.data.model.LoginRequest
import com.cloudfiveapp.android.data.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}
