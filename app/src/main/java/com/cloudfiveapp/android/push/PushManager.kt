package com.cloudfiveapp.android.push

import android.content.Context
import com.cloudfiveapp.android.BuildConfig
import com.cloudfiveapp.push.CloudFivePush
import timber.log.Timber

object PushManager {

    @JvmStatic
    fun configure(context: Context) {
        val pushMessageReceiver = PushNotificationReceiver()
        val devMode = BuildConfig.CLOUDFIVE_DEV == "true"
        CloudFivePush.configure(context.applicationContext, pushMessageReceiver, devMode)
    }

    @JvmStatic
    fun register(userIdentifier: String?) {
        if (userIdentifier != null) {
            Timber.i("Registering for CloudFive as $userIdentifier")
            CloudFivePush.register(userIdentifier)
        } else {
            Timber.i("Registering for CloudFive anonymously")
            CloudFivePush.register()
        }
    }

    @JvmStatic
    fun unregister(userIdentifier: String?) {
        if (userIdentifier != null) {
            Timber.i("Unregistering from CloudFive as $userIdentifier")
            CloudFivePush.unregister(userIdentifier)
        } else {
            Timber.i("Unregistering from CloudFive anonymously")
            CloudFivePush.unregister()
        }
    }
}
