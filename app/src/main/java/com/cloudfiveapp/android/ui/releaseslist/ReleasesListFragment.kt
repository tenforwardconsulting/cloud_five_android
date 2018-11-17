package com.cloudfiveapp.android.ui.releaseslist

import android.Manifest
import android.app.DownloadManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.cloudfiveapp.android.BuildConfig
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseFragment
import com.cloudfiveapp.android.application.injection.Injector
import com.cloudfiveapp.android.data.ProductId
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Release
import com.cloudfiveapp.android.util.extensions.get
import com.cloudfiveapp.android.util.extensions.toUri
import com.cloudfiveapp.android.util.extensions.toastNetworkError
import com.cloudfiveapp.android.util.extensions.visible
import kotlinx.android.synthetic.main.fragment_releases_list.view.*
import timber.log.Timber
import java.io.File

private const val REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 100

class ReleasesListFragment
    : BaseFragment(),
      ReleaseInteractor {

    private val downloadManager: DownloadManager by lazy {
        getSystemService(requireContext(), DownloadManager::class.java) as DownloadManager
    }

    private val releasesAdapter = ReleasesAdapter()

    private var releaseToDownload: Release? = null

    private lateinit var productId: ProductId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = ReleasesListFragmentArgs.fromBundle(arguments).productId
        releasesAdapter.interactor = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_releases_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.releasesRecycler.adapter = releasesAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelFactory = Injector.get().releasesListViewModelFactory()
        val viewModel = viewModelFactory.get(this, ReleasesListViewModel::class)
        bindToViewModel(viewModel)
    }

    private fun bindToViewModel(viewModel: ReleasesListViewModel) {
        viewModel.getReleases(productId)

        view?.releasesSwipeRefresh?.setOnRefreshListener {
            viewModel.refreshReleases()
        }

        viewModel.releases.observe(viewLifecycleOwner, Observer { outcome ->
            val view = view ?: return@Observer
            Timber.d("outcome: $outcome")
            when (outcome) {
                is Outcome.Loading -> {
                    view.releasesSwipeRefresh.isRefreshing = outcome.loading
                }
                is Outcome.Success -> {
                    releasesAdapter.submitList(outcome.data)
                    view.releasesEmptyText.visible(outcome.data.isEmpty())
                }
                is Outcome.Error -> {
                    context?.toastNetworkError(outcome.message ?: outcome.error?.message)
                }
            }
        })
    }

    // region ReleaseInteractor

    override fun onDownloadClicked(release: Release) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            releaseToDownload = release
            requestWriteExternalStoragePermission()
            return
        }
        enqueueReleaseDownload(release)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // try again
                // https://github.com/mozilla-mobile/focus-android/blob/1f1fde04f8db64742cb9f985c9db7264b76df3b5/app/src/main/java/org/mozilla/focus/fragment/BrowserFragment.java#L710
                releaseToDownload?.let(this::enqueueReleaseDownload)
                releaseToDownload = null
            } else {
                // denied
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
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

    private fun requestWriteExternalStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            MaterialDialog(requireContext())
                    .message(R.string.dialog_message_write_storage_permission_rationale)
                    .negativeButton(android.R.string.cancel)
                    .positiveButton(android.R.string.ok) {
                        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE)
                    }
                    .show()
        } else {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE)
        }
    }

    // endregion
}
