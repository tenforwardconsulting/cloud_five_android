package com.cloudfiveapp.android.application.injection

import android.content.Context
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.data.ApiErrorConverter
import com.cloudfiveapp.android.data.remote.LoginApi
import com.cloudfiveapp.android.ui.login.LoginViewModelFactory
import com.cloudfiveapp.android.data.remote.ProductsApi
import com.cloudfiveapp.android.ui.productslist.ProductsListViewModelFactory
import com.cloudfiveapp.android.data.remote.ReleasesApi
import com.cloudfiveapp.android.ui.releaseslist.ApkDownloader
import com.cloudfiveapp.android.ui.releaseslist.ReleasesListViewModelFactory
import dagger.Component
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    ApiModule::class,
    RepositoryModule::class,
    MockNetworkModule::class,
    MockApiModule::class
])
interface AppComponent {

    fun application(): CloudFiveApp

    fun context(): Context

    fun apkDownloader(): ApkDownloader

    fun retrofit(): Retrofit

    fun apiErrorConverter(): ApiErrorConverter

    fun loginApi(): LoginApi

    fun loginViewModelFactory(): LoginViewModelFactory

    fun productsApi(): ProductsApi

    fun productsListViewModelFactory(): ProductsListViewModelFactory

    fun releasesApi(): ReleasesApi

    fun releasesListViewModelFactory(): ReleasesListViewModelFactory

    // mock

    fun mockRetrofit(): MockRetrofit
}
