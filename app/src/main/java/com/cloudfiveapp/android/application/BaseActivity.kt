package com.cloudfiveapp.android.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cloudfiveapp.android.BuildConfig
import com.cloudfiveapp.push.CloudFivePush
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PREFIX = "com.cloudfiveapp.android"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CloudFivePush.configure(this, BuildConfig.GCM_SENDER_ID)

        //        if (this::class != LoginActivity::class) {
        //            val rootView = findViewById<ViewGroup>(android.R.id.content)
        //            lifecycle.addObserver(CloudAnimator(rootView, layoutInflater, true))
        //        }
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
