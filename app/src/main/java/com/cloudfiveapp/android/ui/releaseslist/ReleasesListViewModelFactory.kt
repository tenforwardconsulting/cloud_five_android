package com.cloudfiveapp.android.ui.releaseslist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReleasesListViewModelFactory
@Inject constructor(private val releasesRepository: ReleasesListContract.Repository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReleasesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReleasesListViewModel(releasesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
