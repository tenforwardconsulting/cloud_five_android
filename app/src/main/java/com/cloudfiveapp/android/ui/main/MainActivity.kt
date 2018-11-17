package com.cloudfiveapp.android.ui.main

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.ui.login.LoginActivity
import com.cloudfiveapp.android.ui.releaseslist.ReleaseDownloadBroadcastReceiver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    companion object {
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

        val sessionViewModel = ViewModelProviders.of(this).get(SessionViewModel::class.java)

        sessionViewModel.getSession().observe(this, Observer { loggedIn ->
            if (!loggedIn) {
                finish()
                startActivity(LoginActivity.newIntent(this))
            }
        })

        mainToolbar.inflateMenu(R.menu.main)

        mainToolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.mainLogOut) {
                SessionManager.loggedIn = false
            }
            true
        }

        mainToolbar.setupWithNavController(navController)

        navController.addOnNavigatedListener { _, destination ->
            mainToolbar.menu.findItem(R.id.mainLogOut).isVisible =
                    destination.id == R.id.productsListFragment
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
