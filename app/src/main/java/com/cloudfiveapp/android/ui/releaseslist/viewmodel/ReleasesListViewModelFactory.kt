package com.cloudfiveapp.android.ui.releaseslist.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.cloudfiveapp.android.ui.releaseslist.model.ApkDownloader
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReleasesListViewModelFactory
@Inject constructor(private val releasesRepository: ReleasesListContract.Repository,
                    private val apkDownloader: ApkDownloader)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReleasesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReleasesListViewModel(releasesRepository, apkDownloader) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
