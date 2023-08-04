package com.cloudfiveapp.android.util.extensions

import com.cloudfiveapp.android.data.ApiErrorConverter
import com.cloudfiveapp.android.data.model.Outcome
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Convert this [Response] to an [Outcome].
 */
fun <T> Response<T>.toOutcome(errorConverter: ApiErrorConverter): Outcome<T> {
    val body = body()
    return if (isSuccessful && body != null) {
        Outcome.success(body)
    } else {
        val errorBody = errorBody()
        if (errorBody != null) {
            try {
                val message = errorConverter.convert(errorBody)!!.message
                Outcome.error<T>(null, message)
            } catch (ioEx: IOException) {
                Outcome.error<T>(ioEx)
            }
        } else {
            // TODO: Report this to rollbar because I think this can't actually happen
            // One of body() or errorBody() must be non-null
            Outcome.error(message = "errorBody was null")
        }
    }
}

fun <T> Call<T>.enqueue(success: (response: Response<T>) -> Unit,
                        failure: (t: Throwable) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            success(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            failure(t)
        }
    })
}
