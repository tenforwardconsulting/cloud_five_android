package com.cloudfiveapp.android.util.extensions

import com.cloudfiveapp.android.ui.common.networking.ApiErrorConverter
import com.cloudfiveapp.android.ui.common.networking.Outcome
import java.io.IOException

/**
 * Does a bunch of stuff to convert a Retrofit/RxJava
 * [Outcome][retrofit2.adapter.rxjava2.Result] to a CloudFiveApp [Outcome]
 */
fun <T> retrofit2.adapter.rxjava2.Result<T>.toResult(errorConverter: ApiErrorConverter): Outcome<T> {
    return if (isError) {
        Outcome.error(error())
    } else {
        val response = response()
        if (response != null) {
            val data = response.body()
            if (response.isSuccessful && data != null) {
                Outcome.success(data)
            } else {
                val errorBody = response.errorBody()
                if (errorBody != null) {
                    try {
                        val message = errorConverter.convert(errorBody).message
                        Outcome.error<T>(null, message)
                    } catch (e: IOException) {
                        Outcome.error<T>(e)
                    }
                } else {
                    Outcome.error(message = "errorBody was null")
                }
            }
        } else {
            Outcome.error(message = "Not error and response == null")
        }
    }
}
