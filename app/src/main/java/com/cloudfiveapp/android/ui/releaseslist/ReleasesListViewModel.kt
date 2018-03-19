package com.cloudfiveapp.android.ui.releaseslist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.cloudfiveapp.android.data.ProductId
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Release
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
