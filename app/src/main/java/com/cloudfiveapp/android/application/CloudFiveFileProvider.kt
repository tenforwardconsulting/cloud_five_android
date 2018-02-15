package com.cloudfiveapp.android.application

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import com.cloudfiveapp.android.BuildConfig
import java.io.File

class CloudFiveFileProvider : FileProvider() {

    companion object {
        private const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.fileprovider"

        fun getUriForFile(context: Context, file: File): Uri {
            return FileProvider.getUriForFile(context, AUTHORITY, file)
        }

        fun getApkFile(context: Context, fileName: String): File {
            val filesDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "cloud_five")
            val file = File(filesDir, fileName)
            file.parentFile.mkdirs()
            return file
        }
    }
}
