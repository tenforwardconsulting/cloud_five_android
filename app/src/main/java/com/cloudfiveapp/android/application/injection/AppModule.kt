package com.cloudfiveapp.android.application.injection

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
}
