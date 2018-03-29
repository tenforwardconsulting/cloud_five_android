package com.cloudfiveapp.android.util.extensions

import android.app.Activity
import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast
import com.cloudfiveapp.android.R

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Context.toast(@StringRes messageRes: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, messageRes, length).show()
}

fun Activity.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Activity.toast(@StringRes messageRes: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, messageRes, length).show()
}

fun Activity.toastNetworkError(message: String?, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message ?: getString(R.string.network_error_message), length).show()
}
