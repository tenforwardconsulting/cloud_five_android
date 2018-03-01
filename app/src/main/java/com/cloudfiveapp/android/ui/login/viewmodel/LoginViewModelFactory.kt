package com.cloudfiveapp.android.ui.login.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.cloudfiveapp.android.ui.login.data.LoginApi
import io.reactivex.disposables.CompositeDisposable

class LoginViewModelFactory(private val loginApi: LoginApi,
                            private val compositeDisposable: CompositeDisposable)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(loginApi, compositeDisposable) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
