package com.cloudfiveapp.android.ui.releaseslist

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.data.ProductId
import kotlinx.android.synthetic.main.activity_releases_list.*

class ReleasesListActivity
    : BaseActivity() {

    companion object {
        private const val EXTRA_PRODUCT_ID = "$EXTRA_PREFIX.product_id"
        private const val FRAG_TAG_RELEASES_LIST = "FRAG_TAG_RELEASES_LIST"

        fun newIntent(context: Context, productId: ProductId): Intent {
            return Intent(context, ReleasesListActivity::class.java)
                    .putExtra(EXTRA_PRODUCT_ID, productId)
        }
    }

    private val downloadManager: DownloadManager by lazy {
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private val releaseDownloadBroadcastReceiver: ReleaseDownloadBroadcastReceiver by lazy {
        ReleaseDownloadBroadcastReceiver(releasesCoordinator, this, downloadManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_releases_list)

        val productId = getExtra<ProductId>(EXTRA_PRODUCT_ID) ?: return

        if (savedInstanceState == null) {
            val fragment = ReleasesListFragment.newInstance(productId)
            supportFragmentManager.beginTransaction()
                    .add(R.id.releasesContainer, fragment, FRAG_TAG_RELEASES_LIST)
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
