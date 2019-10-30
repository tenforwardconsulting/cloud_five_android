package com.cloudfiveapp.android.push

import android.content.Context
import android.util.Log
import com.cloudfiveapp.android.BuildConfig
import com.cloudfiveapp.push.CloudFivePush

object PushManager {

    const val INTENT_BADGE_STATUS_NOTIFICATION = "badge_status_notification"
    const val EXTRA_BADGE_ID = "com.disastersolutions.virtualbadge.extras.badge_id"
    private const val TAG = "VB/PushManager"

    @JvmStatic
    fun configure(context: Context) {
        val pushMessageReceiver = PushNotificationReceiver(context.applicationContext)
        val devMode = BuildConfig.CLOUDFIVE_DEV == "true"
        CloudFivePush.configure(context.applicationContext, pushMessageReceiver, devMode)
    }

    @JvmStatic
    fun register(userIdentifier: String?) {
        if (userIdentifier != null) {
            Log.i(TAG, "Registering for CloudFive as " + userIdentifier)
            CloudFivePush.register(userIdentifier)
        } else {
            CloudFivePush.register()
        }
    }

    @JvmStatic
    fun unregister(userIdentifier: String?) {
        if (userIdentifier != null) {
            Log.i(TAG, "Unregistering from CloudFive as " + userIdentifier)
            CloudFivePush.unregister(userIdentifier)
        } else {
            CloudFivePush.unregister()
        }
    }
}