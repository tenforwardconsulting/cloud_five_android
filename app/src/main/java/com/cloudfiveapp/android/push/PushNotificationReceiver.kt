package com.cloudfiveapp.android.push

import android.content.Intent
import com.cloudfiveapp.push.PushMessageReceiver
import timber.log.Timber

class PushNotificationReceiver : PushMessageReceiver {

    override fun onPushMessageReceived(intent: Intent) {
        val alert = intent.extras?.getString("alert")
        val message = intent.extras?.getString("message")
        Timber.d("Got a push with Alert: $alert and Message: $message")
    }
}
