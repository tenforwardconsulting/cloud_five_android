package com.cloudfiveapp.android.application.di

import android.app.DownloadManager
import android.content.Context
import com.cloudfiveapp.android.application.CloudFiveApp
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    fun application(): CloudFiveApp

    fun context(): Context

    fun downloadManager(): DownloadManager

    fun retrofit(): Retrofit
}
