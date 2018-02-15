package com.cloudfiveapp.android.ui.releaseslist.viewmodel

import android.app.DownloadManager
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract
import io.reactivex.disposables.CompositeDisposable

class ReleasesListViewModelFactory(private val application: CloudFiveApp,
                                   private val releasesRepository: ReleasesListContract.Repository,
                                   private val downloadManager: DownloadManager,
                                   private val compositeDisposable: CompositeDisposable)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReleasesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReleasesListViewModel(application, releasesRepository, downloadManager, compositeDisposable) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
