package com.cloudfiveapp.android.data.remote

import com.cloudfiveapp.android.data.model.LoginRequest
import com.cloudfiveapp.android.data.model.LoginResponse
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Single<Result<LoginResponse>>
}
