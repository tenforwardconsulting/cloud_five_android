package com.cloudfiveapp.android.data.model

sealed class Outcome<T> {

    data class Loading<T>(val loading: Boolean) : Outcome<T>()

    data class Success<T>(val data: T) : Outcome<T>()

    data class Error<T>(val error: Throwable?,
                        val message: String?) : Outcome<T>()

    companion object {
        fun <T> success(data: T) = Success(data)

        fun <T> error(error: Throwable? = null, message: String? = null) = Error<T>(error, message)

        fun <T> loading(loading: Boolean) = Loading<T>(loading)
    }
}
