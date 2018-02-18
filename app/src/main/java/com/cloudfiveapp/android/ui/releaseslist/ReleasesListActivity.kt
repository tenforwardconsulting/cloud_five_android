package com.cloudfiveapp.android.ui.releaseslist

import android.Manifest
import android.app.DownloadManager
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
import com.afollestad.materialdialogs.MaterialDialog
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.ui.releaseslist.di.DaggerReleasesListComponent
import com.cloudfiveapp.android.ui.releaseslist.viewmodel.ReleasesListViewModel
import com.cloudfiveapp.android.ui.releaseslist.viewmodel.ReleasesListViewModelFactory
import com.cloudfiveapp.android.util.toast
import com.cloudfiveapp.android.util.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_releases_list.*
import javax.inject.Inject

class ReleasesListActivity
    : BaseActivity(),
      ReleasesAdapter.ReleaseInteractor {

    companion object {
        private const val REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 100

        fun newIntent(context: Context): Intent {
            return Intent(context, ReleasesListActivity::class.java)
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

    private val downloadCompleteReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                viewModel.downloadComplete(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_releases_list)
        component.inject(this)

        releasesAdapter.interactor = this
        releasesRecycler.adapter = releasesAdapter

        releasesSwipeRefresh.setOnRefreshListener {
            viewModel.refreshReleases()
        }

        viewModel.setProductId("hello")
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(downloadCompleteReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        subscribeToViewModel()
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

    // region ReleasesAdapter.ReleaseInteractor

    override fun onDownloadClicked(release: Release) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestWriteExternalStoragePermission()
            return
        }
        viewModel.downloadRelease(release)
    }

    // endregion

    private fun subscribeToViewModel() {
        viewModel.refreshing
                .doOnSubscribe { compositeDisposable += it }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { refreshing ->
                            releasesSwipeRefresh.isRefreshing = refreshing
                        })

        viewModel.downloadEvents
                .doOnSubscribe { compositeDisposable += it }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { downloadEvent ->
                            when (downloadEvent) {
                                is ReleasesListViewModel.DownloadEvent.DownloadStarted -> {
                                    toast("Downloading ${downloadEvent.release?.name}")
                                }
                                is ReleasesListViewModel.DownloadEvent.DownloadCompleted -> {
                                    Snackbar.make(releasesCoordinator, "Download ready", Snackbar.LENGTH_INDEFINITE)
                                            .setAction("Open") {
                                                downloadEvent.release?.let {
                                                    viewModel.openReleaseFile(it)
                                                }
                                            }
                                            .setActionTextColor(ContextCompat.getColor(this, R.color.white))
                                            .show()
                                }
                            }
                        })

        viewModel.releases
                .doOnSubscribe { compositeDisposable += it }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { releases ->
                            releasesAdapter.setData(releases)
                            releasesEmptyText.visible(releases.isEmpty())
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
