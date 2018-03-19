package com.cloudfiveapp.android.application.injection

import android.app.DownloadManager
import android.content.Context
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.ui.releaseslist.ApkDownloader
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
    fun providesApkDownloader(): ApkDownloader {
        val downloadManager = cloudFiveApp.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return ApkDownloader(cloudFiveApp, downloadManager)
    }
}
