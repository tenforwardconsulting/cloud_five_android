package com.cloudfiveapp.android.ui.main

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.ui.login.LoginActivity
import com.cloudfiveapp.android.ui.productslist.ProductsListFragment
import com.cloudfiveapp.android.ui.releaseslist.ReleaseDownloadBroadcastReceiver
import com.cloudfiveapp.android.ui.releaseslist.ReleasesListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    companion object {
        private val TAG = "MainActivity"

        private const val FRAG_TAG_PRODUCTS_LIST = "FRAG_TAG_PRODUCTS_LIST"
        private const val FRAG_TAG_RELEASES_LIST = "FRAG_TAG_RELEASES_LIST"
    }

    private val downloadManager: DownloadManager by lazy {
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private val releaseDownloadBroadcastReceiver: ReleaseDownloadBroadcastReceiver by lazy {
        ReleaseDownloadBroadcastReceiver(mainRootView, this, downloadManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLoginButton.setOnClickListener {
            startActivity(LoginActivity.newIntent(this))
        }

        mainProductsButton.setOnClickListener {
            val fragment = ProductsListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .add(R.id.mainContent, fragment, FRAG_TAG_PRODUCTS_LIST)
                    .addToBackStack("products_list")
                    .commit()
        }

        mainReleasesButton.setOnClickListener {
            val fragment = ReleasesListFragment.newInstance("fake_id")
            supportFragmentManager.beginTransaction()
                    .add(R.id.mainContent, fragment, FRAG_TAG_RELEASES_LIST)
                    .addToBackStack("releases_list")
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
}
