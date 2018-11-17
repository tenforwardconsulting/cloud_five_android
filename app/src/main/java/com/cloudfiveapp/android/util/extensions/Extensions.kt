package com.cloudfiveapp.android.util.extensions

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.util.*
import kotlin.reflect.KClass

fun String.toUri(): Uri {
    return Uri.parse(this)
}

fun Random.nextFloatInRange(min: Float, max: Float): Float {
    return min + nextFloat() * (max - min)
}

fun <VM : ViewModel> ViewModelProvider.Factory.get(activity: FragmentActivity, modelClass: KClass<VM>): VM {
    return ViewModelProviders.of(activity, this).get(modelClass.java)
}

fun <VM : ViewModel> ViewModelProvider.Factory.get(fragment: Fragment, modelClass: KClass<VM>): VM {
    return ViewModelProviders.of(fragment, this).get(modelClass.java)
}
