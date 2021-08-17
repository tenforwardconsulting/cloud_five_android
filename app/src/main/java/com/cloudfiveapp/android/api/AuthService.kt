package com.cloudfiveapp.android.api

import com.haroldadmin.cnradapter.NetworkResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    // TODO: Create better return value types
    @POST("auth/sign_in")
    suspend fun signIn(@Body authCredentials: AuthCredentials): NetworkResponse<ResponseBody, ResponseBody>
}

@JsonClass(generateAdapter = true)
data class AuthCredentials(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
)
