package com.cloudfiveapp.android.application.di

import android.app.DownloadManager
import android.content.Context
import com.cloudfiveapp.android.application.CloudFiveApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val cloudFiveApp: CloudFiveApp) {

    @Provides
    @Singleton
    fun providesApplication(): CloudFiveApp {
        return cloudFiveApp
    }

    @Provides
    @Singleton
    fun providesContext(): Context {
        return cloudFiveApp
    }

    @Provides
    @Singleton
    fun providesDownloadManager(): DownloadManager {
        return cloudFiveApp.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }
}
