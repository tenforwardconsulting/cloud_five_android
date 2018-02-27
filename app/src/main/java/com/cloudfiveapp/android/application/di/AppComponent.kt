package com.cloudfiveapp.android.application.di

import android.app.DownloadManager
import android.content.Context
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.ui.common.networking.ApiErrorConverter
import dagger.Component
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, MockNetworkModule::class])
interface AppComponent {

    fun application(): CloudFiveApp

    fun context(): Context

    fun downloadManager(): DownloadManager

    fun retrofit(): Retrofit

    fun apiErrorConverter(): ApiErrorConverter

    fun mockRetrofit(): MockRetrofit
}
