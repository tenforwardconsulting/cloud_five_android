package com.cloudfiveapp.android.ui.login.data

import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Single<Result<LoginResponse>>
}
