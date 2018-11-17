package com.cloudfiveapp.android.ui.main

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.ui.login.LoginActivity
import com.cloudfiveapp.android.ui.releaseslist.ReleaseDownloadBroadcastReceiver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    companion object {
        var loggedIn = false

        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val downloadManager: DownloadManager by lazy {
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private val releaseDownloadBroadcastReceiver: ReleaseDownloadBroadcastReceiver by lazy {
        ReleaseDownloadBroadcastReceiver(mainRootView, this, downloadManager)
    }

    private val navController by lazy { findNavController(R.id.navHostFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!loggedIn) {
            startActivity(LoginActivity.newIntent(this))
            finish()
        } else {
            val navHost = NavHostFragment.create(R.navigation.nav_graph)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.navHostFragment, navHost)
                    .setPrimaryNavigationFragment(navHost)
                    .runOnCommit {
                        val appBarConfiguration = AppBarConfiguration(navController.graph)
                        mainToolbar.setupWithNavController(navController, appBarConfiguration)
                    }
                    .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(releaseDownloadBroadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(releaseDownloadBroadcastReceiver)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()
}
