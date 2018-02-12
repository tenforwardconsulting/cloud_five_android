package com.cloudfiveapp.android.application

import android.app.Application
import android.os.StrictMode
import com.cloudfiveapp.android.BuildConfig
import com.cloudfiveapp.android.application.di.AppComponent
import com.cloudfiveapp.android.application.di.AppModule
import com.cloudfiveapp.android.application.di.DaggerAppComponent
import com.facebook.stetho.Stetho
import io.reactivex.Completable
import timber.log.Timber

class CloudFiveApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
        initStetho()
        initStrictMode()
        initTimber()
    }

    private fun initDI() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Completable.fromRunnable { Stetho.initializeWithDefaults(this) }
        }
    }

    private fun initStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
