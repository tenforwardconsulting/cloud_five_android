package com.cloudfiveapp.android.ui.login.viewmodel

import com.cloudfiveapp.android.BaseViewModelTest
import com.cloudfiveapp.android.ui.login.data.LoginApi
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.mockito.Mock

class LoginViewModelTest : BaseViewModelTest() {

    @Mock
    private lateinit var loginApi: LoginApi

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(loginApi, compositeDisposable)
    }
}
