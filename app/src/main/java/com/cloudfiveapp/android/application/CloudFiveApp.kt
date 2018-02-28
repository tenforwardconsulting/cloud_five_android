package com.cloudfiveapp.android.application

import android.app.Application
import android.os.StrictMode
import com.cloudfiveapp.android.BuildConfig
import com.cloudfiveapp.android.application.di.AppComponent
import com.cloudfiveapp.android.application.di.AppModule
import com.cloudfiveapp.android.application.di.DaggerAppComponent
import com.cloudfiveapp.android.application.di.NetworkModule
import com.cloudfiveapp.android.application.networking.DeprecatedApiHandler
import com.facebook.stetho.Stetho
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import timber.log.Timber

class CloudFiveApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
        initStrictMode()
        initStetho()
        initTimber()
    }

    private fun initDI() {
        // Cheat a little and init the OkHttp Cache before initializing StrictMode to avoid the
        // warning about accessing the disk on the main thread.
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(cacheDir, cacheSize.toLong())

        val deprecatedApiHandler = object : DeprecatedApiHandler {
            override fun onDeprecatedApi() {
                Timber.d("TODO: Handle deprecated API")
            }
        }

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule(cache, deprecatedApiHandler))
                .build()
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Completable.fromRunnable { Stetho.initializeWithDefaults(this) }
                    .subscribeOn(Schedulers.io())
                    .subscribe()
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
