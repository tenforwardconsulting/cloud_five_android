package com.cloudfiveapp.android.ui.releaseslist.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.cloudfiveapp.android.ui.common.networking.Outcome
import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract
import com.cloudfiveapp.android.util.extensions.toLiveData
import io.reactivex.disposables.CompositeDisposable

class ReleasesListViewModel(private val releasesRepository: ReleasesListContract.Repository,
                            val apkDownloader: ReleasesListContract.ApkDownloader,
                            private val compositeDisposable: CompositeDisposable)
    : ViewModel() {

    val releases: LiveData<Outcome<List<Release>>> by lazy {
        releasesRepository.releasesOutcome.toLiveData(compositeDisposable)
    }

    fun getReleasesFor(productId: String?) {
        if (productId == null)
            return

        // TODO: this ain't right. Should also check the last product id
        // Could possibly wrap List<Release> with another object containing product id
        if (releases.value == null) {
            releasesRepository.refreshReleasesFor(productId)
        }
    }

    fun refreshReleasesFor(productId: String?) {
        if (productId != null) {
            releasesRepository.refreshReleasesFor(productId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
