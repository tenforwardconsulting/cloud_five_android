package com.cloudfiveapp.android.application.injection

import com.cloudfiveapp.android.application.CloudFiveApp

object Injector {

    fun get(): AppComponent {
        return CloudFiveApp.appComponent
    }
}
