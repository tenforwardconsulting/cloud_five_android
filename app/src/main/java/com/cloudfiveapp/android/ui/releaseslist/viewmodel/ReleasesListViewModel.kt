package com.cloudfiveapp.android.ui.releaseslist.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.cloudfiveapp.android.ui.common.data.ProductId
import com.cloudfiveapp.android.ui.common.networking.Outcome
import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract
import com.cloudfiveapp.android.util.extensions.toLiveData
import io.reactivex.disposables.CompositeDisposable

class ReleasesListViewModel(private val releasesRepository: ReleasesListContract.Repository)
    : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var productId: ProductId? = null

    val releases: LiveData<Outcome<List<Release>>> by lazy {
        releasesRepository.releasesOutcome.toLiveData(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getReleases(productId: ProductId) {
        if (this.productId != productId || releases.value == null) {
            this.productId = productId
            releasesRepository.refreshReleases(productId)
        }
    }

    fun refreshReleases() {
        productId?.let {
            releasesRepository.refreshReleases(it)
        }
    }
}
