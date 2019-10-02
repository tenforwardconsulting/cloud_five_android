package com.cloudfiveapp.android.application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cloudfiveapp.push.CloudFivePush
import com.cloudfiveapp.push.PushMessageReceiver
import com.google.firebase.FirebaseApp
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PREFIX = "com.cloudfiveapp.android"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CloudFivePush.configure(this, object : PushMessageReceiver {
            override fun onPushMessageReceived(intent: Intent) {
                println("Received a cloudfive push notification: ${intent.extras}")
            }
        });

        CloudFivePush.register();
    }

    /**
     * Get a required extra passed into this activity, logging an error
     * and calling `finish` if the extra is null.
     *
     * Example usage:
     *
     * `val myStringExtra: String = getExtra("my_string_extra") ?: return`
     */
    protected inline fun <reified T : Any> getExtra(key: String): T? {
        return (intent?.extras?.get(key) as? T) ?: run {
            Timber.e("${this::class.java.simpleName} started with invalid or missing extra $key")
            finish()
            null
        }
    }
}
