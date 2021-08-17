package com.cloudfiveapp.android.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudfiveapp.android.api.AuthCredentials
import com.cloudfiveapp.android.api.AuthService
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val handle: SavedStateHandle
) : ViewModel() {

    init {
        Timber.d("LoginViewModel is alive!")
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val authCredentials = AuthCredentials(email, password)
            when (val response = authService.signIn(authCredentials)) {
                is NetworkResponse.Success -> {
                    Timber.d("Logged in! ${response.headers?.get("access-token")}")
                    onSuccess()
                }
                is NetworkResponse.ServerError -> {
                    Timber.d("5 hundo -> $response")
                }
                is NetworkResponse.NetworkError -> {
                    Timber.e("network error! ${response.error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    Timber.e("something else! - ${response.error.message}")
                }
            }
        }
    }
}
