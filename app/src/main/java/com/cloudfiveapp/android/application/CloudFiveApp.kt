package com.cloudfiveapp.android.application

import android.app.Application
import android.os.StrictMode
import com.cloudfiveapp.android.BuildConfig
import timber.log.Timber

class CloudFiveApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initStrictMode()
        initTimber()
    }

    private fun initStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .permitDiskReads()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
