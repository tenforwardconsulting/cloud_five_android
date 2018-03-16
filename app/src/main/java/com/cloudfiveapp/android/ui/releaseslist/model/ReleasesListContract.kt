package com.cloudfiveapp.android.ui.releaseslist.model

import com.cloudfiveapp.android.ui.common.data.ProductId
import com.cloudfiveapp.android.ui.common.networking.Outcome
import com.cloudfiveapp.android.ui.releaseslist.data.Release
import io.reactivex.subjects.PublishSubject

class ReleasesListContract private constructor() {

    interface Repository {
        val releasesOutcome: PublishSubject<Outcome<List<Release>>>
        fun refreshReleases(productId: ProductId)
    }
}
