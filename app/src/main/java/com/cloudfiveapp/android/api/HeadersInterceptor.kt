package com.cloudfiveapp.android.api

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeadersInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().run {
            addHeader("X-Cloudfive-App-Api-Version", "1")
            build()
        }
        return chain.proceed(request)
    }
}
