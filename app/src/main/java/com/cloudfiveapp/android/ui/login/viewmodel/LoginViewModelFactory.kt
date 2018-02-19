package com.cloudfiveapp.android.ui.login.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.cloudfiveapp.android.ui.login.data.LoginApi

class LoginViewModelFactory(private val loginApi: LoginApi)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(loginApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
