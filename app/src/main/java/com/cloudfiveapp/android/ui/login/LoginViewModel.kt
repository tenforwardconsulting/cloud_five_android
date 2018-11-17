package com.cloudfiveapp.android.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudfiveapp.android.data.ApiErrorConverter
import com.cloudfiveapp.android.data.model.LoginRequest
import com.cloudfiveapp.android.data.model.LoginResponse
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.remote.LoginApi
import com.cloudfiveapp.android.util.extensions.enqueue
import com.cloudfiveapp.android.util.extensions.toOutcome

class LoginViewModel(private val loginApi: LoginApi,
                     private val errorConverter: ApiErrorConverter)
    : ViewModel() {

    private val internal = MutableLiveData<Outcome<LoginResponse>>()

    val loginViewState: LiveData<Outcome<LoginResponse>> = internal

    fun login(email: String, password: String) {
        internal.value = Outcome.loading(true)
        loginApi.login(LoginRequest(email, password))
                .enqueue(
                        success = { response ->
                            internal.value = Outcome.loading(false)
                            internal.value = response.toOutcome(errorConverter)
                        },
                        failure = {
                            internal.value = Outcome.loading(false)
                            internal.value = Outcome.error(it)
                        })
    }
}
