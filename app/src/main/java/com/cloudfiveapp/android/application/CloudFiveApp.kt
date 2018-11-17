package com.cloudfiveapp.android.application

import android.app.Application
import android.os.StrictMode
import com.cloudfiveapp.android.BuildConfig
import com.cloudfiveapp.android.application.injection.AppComponent
import com.cloudfiveapp.android.application.injection.AppModule
import com.cloudfiveapp.android.application.injection.DaggerAppComponent
import com.cloudfiveapp.android.application.injection.NetworkModule
import okhttp3.Cache
import timber.log.Timber

class CloudFiveApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
        initStrictMode()
        initTimber()
    }

    private fun initDI() {
        // Cheat a little and init the OkHttp Cache before initializing StrictMode to avoid the
        // warning about accessing the disk on the main thread.
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(cacheDir, cacheSize.toLong())

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule(cache))
                .build()
    }

    private fun initStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .permitDiskReads()
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
