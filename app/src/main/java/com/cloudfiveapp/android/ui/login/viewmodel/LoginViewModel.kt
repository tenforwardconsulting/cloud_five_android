package com.cloudfiveapp.android.ui.login.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.cloudfiveapp.android.ui.login.data.LoginApi
import com.cloudfiveapp.android.ui.login.data.LoginRequest
import com.cloudfiveapp.android.ui.login.viewmodel.LoginViewModel.LoginViewState.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class LoginViewModel(private val loginApi: LoginApi,
                     private val compositeDisposable: CompositeDisposable)
    : ViewModel() {

    private val loginAttempts = PublishSubject.create<LoginRequest>()

    val loginViewState: LiveData<LoginViewState> by lazy {
        val data = MutableLiveData<LoginViewState>()
        loginAttempts
                .flatMap { loginRequest ->
                    loginApi.login(loginRequest)
                            .map { result ->
                                val response = result.response()
                                val error = result.error()

                                if (error != null) {
                                    NetworkError(error)
                                } else if (response != null) {
                                    val loginResponse = response.body()
                                    if (response.isSuccessful && loginResponse != null) {
                                        LoggedIn(loginResponse.authenticationToken)
                                    } else {
                                        // TODO: Get message from server
                                        InvalidCredentials("Invalid email/password combination")
                                    }
                                } else {
                                    throw UnknownError("result.response == null && result.error == null")
                                }
                            }
                            .toObservable()
                            .startWith(Loading)

                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { loginViewState ->
                            data.value = loginViewState
                        })
                .addTo(compositeDisposable)
        data
    }

    fun login(email: String, password: String) {
        loginAttempts.onNext(LoginRequest(email, password))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    sealed class LoginViewState {
        object Loading : LoginViewState()
        class LoggedIn(val authenticationToken: String) : LoginViewState()
        class InvalidCredentials(val message: String) : LoginViewState()
        class NetworkError(val throwable: Throwable?) : LoginViewState()
    }
}
