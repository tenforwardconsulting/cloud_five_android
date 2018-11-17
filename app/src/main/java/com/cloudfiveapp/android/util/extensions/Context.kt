package com.cloudfiveapp.android.util.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.cloudfiveapp.android.R

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Context.toast(@StringRes messageRes: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, messageRes, length).show()
}

fun Context.toastNetworkError(message: String?, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message ?: getString(R.string.network_error_message), length).show()
}
