package com.cloudfiveapp.android.data.remote.mock

import com.cloudfiveapp.android.data.model.LoginRequest
import com.cloudfiveapp.android.data.model.LoginResponse
import com.cloudfiveapp.android.data.remote.LoginApi
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.Calls

class MockLoginApi(private val delegate: BehaviorDelegate<LoginApi>)
    : LoginApi {

    override fun login(loginRequest: LoginRequest): Single<Result<LoginResponse>> {
        return if (loginRequest.email == "test@test.com" && loginRequest.password == "test") {
            delegate.returningResponse(LoginResponse("test_auth_token"))
                    .login(loginRequest)
        } else {
            val responseBody = ResponseBody.create(MediaType.parse("application/json"), "{ message: \"Invalid creds yo\" }")
            val response = Response.error<LoginResponse>(401, responseBody)
            delegate.returning(Calls.response(response)).login(loginRequest)
        }
    }
}
