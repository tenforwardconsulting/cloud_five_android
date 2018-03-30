package com.cloudfiveapp.android.util.extensions

import android.content.Context
import android.content.Intent

fun activitiesForIntent(context: Context, intent: Intent): Boolean {
    val packageManager = context.packageManager
    val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
    return resolveInfoList.isNotEmpty()
}
