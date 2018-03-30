package com.cloudfiveapp.android.ui.releaseslist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.cloudfiveapp.android.data.ProductId
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Release

class ReleasesListViewModel(private val releasesRepository: ReleasesListContract.Repository)
    : ViewModel() {

    private var productId: ProductId? = null

    val releases: LiveData<Outcome<List<Release>>> = releasesRepository.releasesOutcome

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
