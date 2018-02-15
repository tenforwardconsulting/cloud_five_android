package com.cloudfiveapp.android.util

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

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

fun String.toUri(): Uri {
    return Uri.parse(this)
}

fun DownloadManager.enqueue(uri: Uri, block: DownloadManager.Request.() -> Unit): Long {
    return enqueue(DownloadManager.Request(uri).apply(block))
}
