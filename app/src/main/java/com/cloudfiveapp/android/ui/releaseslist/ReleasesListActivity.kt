package com.cloudfiveapp.android.ui.releaseslist

import android.Manifest
import android.app.DownloadManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.cloudfiveapp.android.BuildConfig
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.application.injection.Injector
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Release
import com.cloudfiveapp.android.util.extensions.get
import com.cloudfiveapp.android.util.extensions.toUri
import com.cloudfiveapp.android.util.extensions.toastNetworkError
import com.cloudfiveapp.android.util.extensions.visible
import kotlinx.android.synthetic.main.activity_releases_list.*
import timber.log.Timber
import java.io.File

class ReleasesListActivity
    : BaseActivity(),
      ReleaseInteractor {

    companion object {
        private const val REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 100

        fun newIntent(context: Context): Intent {
            return Intent(context, ReleasesListActivity::class.java)
        }
    }

    private val viewModelFactory = Injector.get().releasesListViewModelFactory()

    private val viewModel by lazy {
        viewModelFactory.get(this, ReleasesListViewModel::class)
    }

    private val releasesAdapter = ReleasesAdapter()

    private val downloadManager: DownloadManager by lazy {
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private val releaseDownloadBroadcastReceiver: ReleaseDownloadBroadcastReceiver by lazy {
        ReleaseDownloadBroadcastReceiver(releasesCoordinator, this, downloadManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_releases_list)

        releasesAdapter.interactor = this
        releasesRecycler.adapter = releasesAdapter

        releasesSwipeRefresh.setOnRefreshListener {
            viewModel.refreshReleases()
        }

        bindToViewModel()
        viewModel.getReleases("hello")
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(releaseDownloadBroadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(releaseDownloadBroadcastReceiver)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // try again
                // https://github.com/mozilla-mobile/focus-android/blob/1f1fde04f8db64742cb9f985c9db7264b76df3b5/app/src/main/java/org/mozilla/focus/fragment/BrowserFragment.java#L710
            } else {
                // denied
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    // region ReleaseInteractor

    override fun onDownloadClicked(release: Release) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestWriteExternalStoragePermission()
            return
        }
        enqueueReleaseDownload(release)
    }

    private fun enqueueReleaseDownload(release: Release) {
        val request = DownloadManager.Request(release.downloadUrl.toUri()).apply {

            val destination = "${File.separator}cloud_five${File.separator}${release.downloadFileName}"
            try {
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, destination)
            } catch (e: IllegalStateException) {
                Timber.e(e, "Unable to set download destination")
                return
            }
            val allowedNetworkTypes = if (BuildConfig.DEBUG) {
                // Allow mobile in debug so emulators can download.
                DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
            } else {
                DownloadManager.Request.NETWORK_WIFI
            }
            setAllowedNetworkTypes(allowedNetworkTypes)
            setTitle("${release.name} Download")
            setDescription("${release.version} - ${release.latestBuildNumber} - ${release.commitHash}")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        }

        downloadManager.enqueue(request)
    }

    // endregion

    private fun bindToViewModel() {
        viewModel.releases.observe(this, Observer { outcome ->
            Timber.d("outcome: $outcome")
            when (outcome) {
                is Outcome.Loading -> {
                    releasesSwipeRefresh.isRefreshing = outcome.loading
                }
                is Outcome.Success -> {
                    releasesAdapter.submitList(outcome.data)
                    releasesEmptyText.visible(outcome.data.isEmpty())
                }
                is Outcome.Error -> {
                    toastNetworkError(outcome.message ?: outcome.error?.message)
                }
            }
        })
    }

    private fun requestWriteExternalStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            MaterialDialog.Builder(this)
                    .content("")
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.cancel)
                    .onPositive { _, _ ->
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE)
                    }
                    .show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE)
        }
    }
}
