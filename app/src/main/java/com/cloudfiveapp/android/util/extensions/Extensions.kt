package com.cloudfiveapp.android.util.extensions

import android.app.DownloadManager
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.support.v4.app.FragmentActivity
import java.util.*
import kotlin.reflect.KClass

fun String.toUri(): Uri {
    return Uri.parse(this)
}

fun DownloadManager.enqueue(uri: Uri, block: DownloadManager.Request.() -> Unit): Long {
    return enqueue(DownloadManager.Request(uri).apply(block))
}

fun Random.nextFloatInRange(min: Float, max: Float): Float {
    return min + nextFloat() * (max - min)
}

fun <VM : ViewModel> ViewModelProvider.Factory.get(activity: FragmentActivity, modelClass: KClass<VM>): VM {
    return ViewModelProviders.of(activity, this).get(modelClass.java)
}
