package com.cloudfiveapp.android.util.extensions

import android.database.Cursor

fun Cursor.getInt(columnName: String): Int = getInt(getColumnIndexOrThrow(columnName))

fun Cursor.getString(columnName: String): String = getString(getColumnIndexOrThrow(columnName))
