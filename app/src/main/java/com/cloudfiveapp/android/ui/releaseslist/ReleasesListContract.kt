package com.cloudfiveapp.android.ui.releaseslist

import androidx.lifecycle.LiveData
import com.cloudfiveapp.android.data.ProductId
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Release

class ReleasesListContract private constructor() {

    interface Repository {
        val releasesOutcome: LiveData<Outcome<List<Release>>>
        fun refreshReleases(productId: ProductId)
    }
}
