package com.cloudfiveapp.android.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.cloudfiveapp.android.push.PushManager
import timber.log.Timber





abstract class BaseActivity : AppCompatActivity(), LifecycleObserver {

    companion object {
        const val EXTRA_PREFIX = "com.cloudfiveapp.android"

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onAppBackgrounded() {
            Timber.d("In Background")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onAppForegrounded() {
            Timber.d("In Foreground")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        PushManager.configure(this)
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
