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
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.ui.common.networking.Outcome
import com.cloudfiveapp.android.ui.releaseslist.adapter.ReleaseInteractor
import com.cloudfiveapp.android.ui.releaseslist.adapter.ReleasesAdapter
import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.ui.releaseslist.di.DaggerReleasesListComponent
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract.ApkDownloader.DownloadEvent.DownloadCompleted
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract.ApkDownloader.DownloadEvent.DownloadStarted
import com.cloudfiveapp.android.ui.releaseslist.viewmodel.ReleasesListViewModel
import com.cloudfiveapp.android.ui.releaseslist.viewmodel.ReleasesListViewModelFactory
import com.cloudfiveapp.android.util.extensions.toast
import com.cloudfiveapp.android.util.extensions.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_releases_list.*
import javax.inject.Inject

class ReleasesListActivity
    : BaseActivity(),
      ReleaseInteractor {

    companion object {
        private const val REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 100

        private const val EXTRA_PRODUCT_ID = "c5.extras.product_id"

        fun newIntent(context: Context, productId: String): Intent {
            return Intent(context, ReleasesListActivity::class.java)
                    .putExtra(EXTRA_PRODUCT_ID, productId)
        }
    }

    private val component by lazy {
        DaggerReleasesListComponent.builder().appComponent(CloudFiveApp.appComponent).build()
    }

    @Inject
    lateinit var viewModelFactory: ReleasesListViewModelFactory

    @Inject
    lateinit var releasesAdapter: ReleasesAdapter

    private val compositeDisposable = CompositeDisposable()

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ReleasesListViewModel::class.java)
    }

    private val apkDownloader: ReleasesListContract.ApkDownloader by lazy {
        viewModel.apkDownloader
    }

    private val downloadCompleteReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                apkDownloader.downloadComplete(intent)
            }
        }
    }

    private val productId: String? by lazy {
        intent.extras?.getString(EXTRA_PRODUCT_ID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_releases_list)
        component.inject(this)

        releasesRecycler.adapter = releasesAdapter
        releasesAdapter.interactor = this

        bindToViewModel()

        viewModel.getReleasesFor(productId)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(downloadCompleteReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
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
        releasesSwipeRefresh.setOnRefreshListener { viewModel.refreshReleasesFor(productId) }

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

        viewModel.releases.observe(this, Observer { outcome ->
            when (outcome) {
                is Outcome.Loading -> releasesSwipeRefresh.isRefreshing = outcome.loading
                is Outcome.Success -> releasesAdapter.setList(outcome.data)
                is Outcome.Error -> {
                    // TODO: Betterify this
                    toast(outcome.message ?: outcome.error?.message
                    ?: "Network error", Toast.LENGTH_LONG)
                }
            }
            releasesEmptyText.visible(releasesAdapter.itemCount == 0)
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
