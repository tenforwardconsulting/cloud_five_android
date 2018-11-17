package com.cloudfiveapp.android.ui.main

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import androidx.navigation.findNavController
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.ui.releaseslist.ReleaseDownloadBroadcastReceiver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val downloadManager: DownloadManager by lazy {
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private val releaseDownloadBroadcastReceiver: ReleaseDownloadBroadcastReceiver by lazy {
        ReleaseDownloadBroadcastReceiver(mainRootView, this, downloadManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(releaseDownloadBroadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(releaseDownloadBroadcastReceiver)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navHostFragment).navigateUp()
}
