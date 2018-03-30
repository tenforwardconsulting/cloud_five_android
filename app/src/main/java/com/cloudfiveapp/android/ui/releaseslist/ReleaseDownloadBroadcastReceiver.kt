package com.cloudfiveapp.android.ui.releaseslist

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.util.extensions.activitiesForIntent
import com.cloudfiveapp.android.util.extensions.getInt
import com.cloudfiveapp.android.util.extensions.getString
import java.io.File

class ReleaseDownloadBroadcastReceiver(private val snackbarTargetView: View,
                                       private val context: Context,
                                       private val downloadManager: DownloadManager)
    : BroadcastReceiver() {

    companion object {
        private const val FILE_SCHEME = "file://"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val downloadReference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        displaySnackbar(downloadReference)
    }

    private fun displaySnackbar(downloadReference: Long) {
        val query = DownloadManager.Query().apply { setFilterById(downloadReference) }
        downloadManager.query(query).use { cursor ->
            if (cursor.moveToFirst()) {
                if (cursor.getInt(DownloadManager.COLUMN_STATUS) == DownloadManager.STATUS_SUCCESSFUL) {
                    val uriString = cursor.getString(DownloadManager.COLUMN_LOCAL_URI)
                    val localUri = if (uriString.startsWith(FILE_SCHEME)) {
                        uriString.substring(FILE_SCHEME.length)
                    } else {
                        uriString
                    }
                    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(localUri)
                    val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
                    val fileName = URLUtil.guessFileName(Uri.decode(localUri), null, mimeType)

                    val file = File(Uri.decode(localUri))
                    val uriForFile = Uri.parse("file://${file.absolutePath}")

                    val intent = Intent(Intent.ACTION_VIEW)
                            .setDataAndType(uriForFile, mimeType)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    or Intent.FLAG_ACTIVITY_NEW_TASK
                                    or Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    showSnackbarForFile(intent, fileName)
                }
            }
        }
    }

    private fun showSnackbarForFile(openFileIntent: Intent, fileName: String) {
        val snackbar = Snackbar.make(snackbarTargetView, fileName, Snackbar.LENGTH_INDEFINITE)
        if (activitiesForIntent(context, openFileIntent)) {
            snackbar.setAction(R.string.snackbar_action_open) {
                context.startActivity(openFileIntent)
            }
            snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white))
        }
        snackbar.show()
    }
}
