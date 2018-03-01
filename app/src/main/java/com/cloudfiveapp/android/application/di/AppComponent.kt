package com.cloudfiveapp.android.application.di

import android.app.DownloadManager
import android.content.Context
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.ui.common.networking.ApiErrorConverter
import com.cloudfiveapp.android.ui.login.data.LoginApi
import com.cloudfiveapp.android.ui.login.viewmodel.LoginViewModelFactory
import dagger.Component
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    MockNetworkModule::class,
    ApiModule::class,
    MockApiModule::class
])
interface AppComponent {

    fun application(): CloudFiveApp

    fun context(): Context

    fun downloadManager(): DownloadManager

    fun retrofit(): Retrofit

    fun apiErrorConverter(): ApiErrorConverter

    fun loginApi(): LoginApi

    fun loginViewModelFactory(): LoginViewModelFactory

    // mock

    fun mockRetrofit(): MockRetrofit

    @Named("mock")
    fun mockLoginApi(): LoginApi
}
