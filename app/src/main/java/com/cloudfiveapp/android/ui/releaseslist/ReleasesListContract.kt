package com.cloudfiveapp.android.ui.releaseslist

import com.cloudfiveapp.android.data.ProductId
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Release
import io.reactivex.subjects.PublishSubject

class ReleasesListContract private constructor() {

    interface Repository {
        val releasesOutcome: PublishSubject<Outcome<List<Release>>>
        fun refreshReleases(productId: ProductId)
    }
}
