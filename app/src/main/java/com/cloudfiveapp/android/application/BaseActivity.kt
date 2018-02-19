package com.cloudfiveapp.android.application

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.cloudfiveapp.android.ui.login.LoginActivity
import com.cloudfiveapp.android.util.CloudAnimator

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (this::class != LoginActivity::class) {
            val rootView = findViewById<ViewGroup>(android.R.id.content)
            lifecycle.addObserver(CloudAnimator(rootView, layoutInflater, true))
        }
    }
}
