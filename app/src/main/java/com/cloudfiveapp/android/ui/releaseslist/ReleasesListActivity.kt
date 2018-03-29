package com.cloudfiveapp.android.ui.releaseslist

import android.Manifest
import android.app.DownloadManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.application.injection.Injector
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Release
import com.cloudfiveapp.android.ui.releaseslist.ApkDownloader.DownloadEvent.DownloadCompleted
import com.cloudfiveapp.android.ui.releaseslist.ApkDownloader.DownloadEvent.DownloadStarted
import com.cloudfiveapp.android.util.extensions.toast
import com.cloudfiveapp.android.util.extensions.toastNetworkError
import com.cloudfiveapp.android.util.extensions.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_releases_list.*
import timber.log.Timber

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
        ViewModelProviders.of(this, viewModelFactory).get(ReleasesListViewModel::class.java)
    }

    private val compositeDisposable = CompositeDisposable()

    private val apkDownloader = Injector.get().apkDownloader()

    private val releasesAdapter = ReleasesAdapter()

    private val downloadCompleteReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                apkDownloader.downloadComplete(intent)
            }
        }
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
        registerReceiver(downloadCompleteReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        subscribeToApkDownloader()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(downloadCompleteReceiver)
        compositeDisposable.clear()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // try again
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
        apkDownloader.downloadRelease(release)
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

    private fun subscribeToApkDownloader() {
        apkDownloader
                .downloadEvents
                .doOnSubscribe { compositeDisposable += it }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { downloadEvent ->
                            when (downloadEvent) {
                                is DownloadStarted -> {
                                    toast("Downloading ${downloadEvent.release?.name}")
                                }
                                is DownloadCompleted -> {
                                    Snackbar.make(releasesCoordinator, "Download ready", Snackbar.LENGTH_INDEFINITE)
                                            .setAction("Open") {
                                                downloadEvent.release?.let {
                                                    apkDownloader.openReleaseFile(it)
                                                }
                                            }
                                            .setActionTextColor(ContextCompat.getColor(this, R.color.white))
                                            .show()
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
